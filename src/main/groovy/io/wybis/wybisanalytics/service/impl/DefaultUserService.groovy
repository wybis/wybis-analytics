package io.wybis.wybisanalytics.service.impl

import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.constants.UserStatus
import io.wybis.wybisanalytics.dto.SessionDto
import io.wybis.wybisanalytics.model.User
import io.wybis.wybisanalytics.repository.UserRepository
import io.wybis.wybisanalytics.service.UserService
import io.wybis.wybisanalytics.service.exceptions.ModelAlreadyExistException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service
@Slf4j
public class DefaultUserService extends AbstractService implements UserService {

    @Resource
    private UserRepository userRepository;

    @Transactional
    @Override
    public void add(SessionDto sessionDto, User model)
            throws ModelAlreadyExistException {

        User aModel = userRepository.findByUserId(model.userId)
        if (aModel) {
            throw new ModelAlreadyExistException()
        }

        model.status = UserStatus.ACTIVE
        model.id = autoNumberService.nextNumber(sessionUser, User.ID_KEY)

        model.createBy = sessionDto.id
        model.updateBy = sessionDto.id
        aModel = this.userRepository.save(model)
        model.createTime = aModel.createTime
        model.updateTime = aModel.updateTime
    }
}
