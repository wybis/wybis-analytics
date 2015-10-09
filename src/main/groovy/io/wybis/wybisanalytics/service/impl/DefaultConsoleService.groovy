package io.wybis.wybisanalytics.service.impl

import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.repository.UserRepository
import io.wybis.wybisanalytics.service.ConsoleService
import org.springframework.stereotype.Service

import javax.annotation.Resource
import javax.servlet.http.HttpSession

@Service
@Slf4j
public class DefaultConsoleService extends AbstractService implements
        ConsoleService {

    @Override
    public void reset(HttpSession session) {
    }

    @Override
    public void clear(HttpSession session) {
    }

    @Override
    public void clearData(HttpSession session) {
    }

}
