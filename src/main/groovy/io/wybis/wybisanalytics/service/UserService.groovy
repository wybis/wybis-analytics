package io.wybis.wybisanalytics.service

import io.wybis.wybisanalytics.dto.SessionDto
import io.wybis.wybisanalytics.model.User
import io.wybis.wybisanalytics.service.exceptions.ModelAlreadyExistException

public interface UserService {

    void add(SessionDto sessionDto, User model) throws ModelAlreadyExistException;
}
