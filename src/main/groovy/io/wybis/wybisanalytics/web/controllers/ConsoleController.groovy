package io.wybis.wybisanalytics.web.controllers

import com.google.common.base.Throwables
import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.dto.ResponseDto
import io.wybis.wybisanalytics.service.ConsoleService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/console")
@Slf4j
public class ConsoleController extends AbstractController {

    @Resource
    ConsoleService consoleService;

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto reset(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        try {
            consoleService.reset(session);
            responseDto.setType(ResponseDto.SUCCESS);
            responseDto.setMessage("Successfully reset...");
        } catch (Throwable t) {
            String s = "Unable to reset...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
            responseDto.setData(Throwables.getStackTraceAsString(t));
            t.printStackTrace();
        }

        return responseDto;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto clear(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        try {
            consoleService.clear(session);
            responseDto.setType(ResponseDto.SUCCESS);
            responseDto.setMessage("Successfully cleared...");
        } catch (Throwable t) {
            String s = "Unable to clear...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
            responseDto.setData(Throwables.getStackTraceAsString(t));
            t.printStackTrace();
        }

        return responseDto;
    }

    @RequestMapping(value = "/clearData", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto clearTransactions(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        try {
            consoleService.clearData(session);
            responseDto.setType(ResponseDto.SUCCESS);
            String s = "Successfully cleared data...";
            responseDto.setMessage(s);
        } catch (Throwable t) {
            String s = "Unable to clear data...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
            responseDto.setData(Throwables.getStackTraceAsString(t));
            t.printStackTrace();
        }

        return responseDto;
    }

}
