package io.wybis.wybisanalytics.web.controllers

import com.google.common.base.Throwables
import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.dto.ResponseDto
import io.wybis.wybisanalytics.repository.AccessDataRepository
import io.wybis.wybisanalytics.utils.Helper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource

@Controller
@RequestMapping("/session/access-data")
@Slf4j
public class AccessDataController {

    @Resource
    AccessDataRepository accessDataRepository

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto search(@RequestParam(value = 'pageNumber', defaultValue = '1') int pageNumber,
                       @RequestParam(value = 'applicationCode') String applicationCode,
                       @RequestParam(value = 'fromMilliSeconds') long fromMilliSeconds,
                       @RequestParam(value = 'toMilliSeconds') long toMilliSeconds) {
        ResponseDto responseDto = new ResponseDto()

        try {
            Date st = new Date(fromMilliSeconds), et = new Date(toMilliSeconds);
            //log.info("{}, {}, {}, {}", pageNumber, applicationCode, st, et);
            PageRequest pageRequest = new PageRequest(pageNumber - 1, 500, Sort.Direction.DESC, 'id');
            responseDto.data = accessDataRepository.findByApplicationCodeAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(applicationCode, st, et, pageRequest)
        }
        catch (Throwable t) {
            responseDto.type = ResponseDto.ERROR
            responseDto.message = t.message
            responseDto.data = Helper.getStackTraceAsString(t)
            log.error("fetching access data failed", t)
        }

        return responseDto;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto list(@RequestParam(value = 'pageNumber', defaultValue = '1') int pageNumber) {
        ResponseDto responseDto = new ResponseDto()

        try {
            PageRequest pageRequest = new PageRequest(pageNumber - 1, 500, Sort.Direction.DESC, 'id');

            responseDto.data = accessDataRepository.findAll(pageRequest)
        }
        catch (Throwable t) {
            responseDto.type = ResponseDto.ERROR
            responseDto.message = t.message
            responseDto.data = Helper.getStackTraceAsString(t)
            log.error("fetching access data failed", t)
        }

        return responseDto;
    }

}
