package io.wybis.wybisanalytics.service

import io.wybis.wybisanalytics.dto.UserDto
import io.wybis.wybisanalytics.service.exceptions.InvalidCredentialException

import javax.servlet.http.HttpSession

public interface SessionService {

    static String SESSION_USER_KEY = "user"

    Map<String, Object> properties(HttpSession session)

    void login(HttpSession session, UserDto user)
            throws InvalidCredentialException

    void logout(HttpSession session)

}
