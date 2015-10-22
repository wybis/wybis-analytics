package io.wybis.wybisanalytics.service.impl

import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.model.AccessData
import io.wybis.wybisanalytics.repository.AccessDataRepository
import io.wybis.wybisanalytics.service.AccessDataService
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.integration.support.MessageBuilder
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Service

import javax.annotation.Resource
import java.util.regex.Matcher
import java.util.regex.Pattern

@Service
@Slf4j
class DefaultAccessDataService implements AccessDataService {

    Pattern filePattern  = Pattern.compile('^([a-z].*)-(\\d+)-(\\d\\d)-(\\d\\d)_\\d\\d_\\d\\d_\\d\\d\\.\\d\\d\\d\\.log')

    Pattern startPattern = Pattern.compile('^(.*)  INFO  (.*) (.*) c.a.h.w.i.TransactionInterceptor - \\/(.*) started\\.\\.\\.$');

    Pattern endPatternOne = Pattern.compile('^(.*)  INFO  (.*) (.*) c.a.h.w.i.TransactionInterceptor - Runtime .* is (.*)$');

    Pattern endPatternTwo = Pattern.compile('^(.*)  INFO  (.*) (.*) c.a.h.w.i.TransactionInterceptor - Runtime : (.*)$');

    DateTimeFormatter datetimeFormatterAndParser = DateTimeFormat.forPattern('YYYY-MM-dd HH:mm:ss.SSS')

    @Resource
    AccessDataRepository accessDataRepository

    @Override
    boolean hasAccessData(String line) {
        Matcher matcher = startPattern.matcher(line)
        if (matcher.matches()) {
            return true
        }
        matcher = endPatternOne.matcher(line)
        if (matcher.matches()) {
            return true
        }
        matcher = endPatternTwo.matcher(line)
        if (matcher.matches()) {
            return true
        }
        return false
    }

    @Override
    AccessData create(String applicationCode, String line) {
        AccessData accessData = null

        Matcher matcher = startPattern.matcher(line)
        //println matcher.matches()
        if (matcher.matches()) {
            accessData = new AccessData()
            accessData.applicationCode = applicationCode
            accessData.startTimeRaw = matcher[0][1]
            accessData.requestId = matcher[0][2]
            accessData.userId = matcher[0][3]
            accessData.requestPath = '/' + matcher[0][4]
            accessData.computeStartTime(datetimeFormatterAndParser)
            return accessData
        }
        matcher = endPatternOne.matcher(line)
        //println matcher.matches()
        if (matcher.matches()) {
            accessData = new AccessData()
            accessData.applicationCode = applicationCode
            accessData.requestId = matcher[0][2]
            accessData.endTimeRaw = matcher[0][1]
            accessData.processTime = Long.parseLong(matcher[0][4])
            accessData.computeEndTime(datetimeFormatterAndParser)
            return accessData
        }
        matcher = endPatternTwo.matcher(line)
        //println matcher.matches()
        if (matcher.matches()) {
            accessData = new AccessData()
            accessData.applicationCode = applicationCode
            accessData.requestId = matcher[0][2]
            accessData.endTimeRaw = matcher[0][1]
            accessData.processTime = Long.parseLong(matcher[0][4])
            accessData.computeEndTime(datetimeFormatterAndParser)
            return accessData
        }

        return accessData
    }

    @Override
    void save(AccessData accessData) {
        if (accessData.endTimeRaw) {
            AccessData ad = accessDataRepository.findByApplicationCodeAndRequestId(accessData.applicationCode, accessData.requestId)
            if (ad) {
                ad.processTime = accessData.processTime
                ad.endTimeRaw = accessData.endTimeRaw
                ad.endTime = accessData.endTime
                ad.computeProcessTimeCalculated()
                accessData = accessDataRepository.save(ad)
            }
        } else {
            AccessData ad = accessDataRepository.findByApplicationCodeAndRequestId(accessData.applicationCode, accessData.requestId)
            if (ad) {
                accessData.id = ad.id
            } else {
                accessData.endTime = accessData.startTime
                accessData.processTime = -1
                accessData.processTimeC = -1
            }
            accessData = accessDataRepository.save(accessData)
        }
    }

    @Override
    void parseAndCreateAndSaveFrom(File logFile) {
        log.info('parsing {} started...', logFile.name)

        log.info('Log  file {} {}', logFile.name, (logFile.exists() ? 'exists' : 'not exists'))
        Matcher matcher = filePattern.matcher(logFile.name)
        //println matcher.matches()
        if (matcher.matches()) {
            String applicationCode = matcher[0][1]
            String logDate = matcher[0][2] + '-' + matcher[0][3] + '-' + matcher[0][4]
            logFile.eachLine { line ->
                line = "$logDate $line"
                if (hasAccessData(line)) {
                    save(create(applicationCode, line))
                }
            }
        } else {
            log.info('invalid log file {}', logFile.name)
        }

        log.info('parsing {} finished...', logFile.name)
    }

    Message<AccessData> createFrom(Message<String> messageIn) {
        Message<String> messageOut = null

        //println(messageIn)
        Matcher matcher = filePattern.matcher(messageIn.headers['file_name'])
        //println matcher.matches()
        if (matcher.matches()) {
            String line = matcher[0][2] + '-' + matcher[0][3] + '-' + matcher[0][4]
            line = line + ' ' + messageIn.payload
            AccessData accessData = create(matcher[0][1], line)
            messageOut = MessageBuilder.withPayload(accessData)
                    .copyHeaders(messageIn.headers)
                    .setHeader('application_code', matcher[0][1])
                    .build()
        } else {
            messageOut = messageIn
        }
        //println messageOut

        return messageOut
    }

    @Resource
    MessageChannel accessDataLogLineInChannel

    void parseX(File logFile) {
        log.info('parsing {} started...', logFile.name)

        log.info('Log  file {} {}', logFile.name, (logFile.exists() ? 'exists' : 'not exists'))
        Matcher matcher = filePattern.matcher(logFile.name)
        println matcher.matches()
        if (matcher.matches()) {
            String logFileDate = matcher[0][1]
            logFile.eachLine { line ->
                line = "$logFileDate $line"
                accessDataLogLineInChannel.send(new GenericMessage<String>(line))
            }
        } else {
            log.info('invalid log file {}', logFile.name)
        }

        log.info('parsing {} finished...', logFile.name)
    }

}
