package io.wybis.wybisanalytics.web.controllers

import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.dto.ResponseDto
import io.wybis.wybisanalytics.dto.UserDto
import io.wybis.wybisanalytics.service.SessionService
import io.wybis.wybisanalytics.service.exceptions.InvalidCredentialException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import javax.annotation.Resource
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/sessions")
@Slf4j
public class SessionServiceController extends AbstractController {

    @Resource
    SessionService sessionService;

    @RequestMapping(value = "/properties", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto properties(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        try {
            Map<String, Object> props = sessionService.properties(session);
            responseDto.setData(props);
            responseDto.setType(ResponseDto.SUCCESS);

        } catch (Throwable t) {
            String s = "Unable to retrive session properties...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
        }

        return responseDto;
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public @ResponseBody
    ResponseDto login(HttpSession session,
                      @RequestBody final UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();

        try {
            sessionService.login(session, userDto);

            responseDto.setType(ResponseDto.SUCCESS);
            responseDto.setMessage("Successfully logged in...");

        } catch (InvalidCredentialException t) {
            String s = "Invalid User Id or Password...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
        }

        return responseDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto logout(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        sessionService.logout(session);

        responseDto.setType(ResponseDto.SUCCESS);
        responseDto.setMessage("Successfulyy logged in...");

        return responseDto;
    }
}
