package io.wybis.wybisanalytics.web.controllers

import com.google.common.collect.Maps
import groovy.util.logging.Slf4j

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.GenericMessage
import org.springframework.mobile.device.Device
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource

@Controller
@Slf4j
public class DefaultController {

    @RequestMapping(value = '/ping', method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> ping() {
        Map<String, String> map = Maps.newHashMap();

        map.put('ping', 'Ping Pong!!!');

        return map;
    }

    String deviceResponseTemplate = 'Hello! Your device is %s!';

    @RequestMapping('/whatIsMyDeviceType')
    public @ResponseBody
    String detectDevice(Device device) {
        String deviceType = 'Unknown';

        if (device.isNormal()) {
            deviceType = 'PC or Laptop or TV';
        } else if (device.isMobile()) {
            deviceType = 'Mobile';
        } else if (device.isTablet()) {
            deviceType = 'Tablet';
        }

        return String.format(this.deviceResponseTemplate, deviceType);
    }

    @Resource
    MessageChannel accessDataLogLineInChannel

    @RequestMapping(value = '/logLine', method = RequestMethod.POST)
    public @ResponseBody
    String logLine(@RequestBody String lines) {
        boolean flag = false

        lines.eachLine { line ->
            Message<String> message = new GenericMessage<String>(line);
            flag = accessDataLogLineInChannel.send(message)
        }

        return flag
    }

    @Resource
    MessageChannel testChannel

    @RequestMapping(value = '/testChannel', method = RequestMethod.GET)
    public @ResponseBody
    String testChannel(@RequestParam(value = 'message', defaultValue = 'Welcome Test Channel!') String messageValue) {
        boolean flag = false

        Message<String> message = new GenericMessage<String>(messageValue);
        flag = testChannel.send(message)

        return flag == true ? 'Successfully sent..' : 'Unable to send...'
    }

}
