package io.wybis.wybisanalytics.service.impl

import com.google.common.collect.Maps
import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.dto.SessionDto
import io.wybis.wybisanalytics.dto.UserDto
import io.wybis.wybisanalytics.model.User
import io.wybis.wybisanalytics.repository.UserRepository
import io.wybis.wybisanalytics.service.SessionService
import io.wybis.wybisanalytics.service.exceptions.InvalidCredentialException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import javax.servlet.http.HttpSession

@Service
@Slf4j
public class DefaultSessionService extends AbstractService implements
        SessionService {

    @Resource
    private UserRepository userRepository;

    @Override
    public Map<String, Object> properties(HttpSession session) {
        Map<String, Object> props = Maps.newHashMap()

        props.put("user", session.getAttribute("user"))

        return props
    }

    @Transactional(readOnly = true)
    @Override
    public void login(HttpSession session, UserDto userDto)
            throws InvalidCredentialException {
        User aUser = userRepository.findByUserId(userDto.userId)

        if (aUser == null || aUser.password != userDto.password) {
            throw new InvalidCredentialException()
        }

        SessionDto sessionDto = new SessionDto()
        sessionDto.with {
            id = aUser.id
            userId = aUser.userId
            firstName = aUser.firstName
            lastName = aUser.lastName
            type = aUser.type
            roleId = aUser.roleId
        }

        session.setAttribute(SESSION_USER_KEY, sessionDto)
    }

    @Override
    public void logout(HttpSession session) {

        session.removeAttribute(SESSION_USER_KEY)

    }
}
