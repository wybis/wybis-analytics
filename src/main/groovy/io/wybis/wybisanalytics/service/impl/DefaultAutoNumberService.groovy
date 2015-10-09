package io.wybis.wybisanalytics.service.impl

import groovy.util.logging.Slf4j
import io.wybis.wybisanalytics.dto.SessionDto
import io.wybis.wybisanalytics.model.AutoNumber
import io.wybis.wybisanalytics.repository.AutoNumberRepository
import io.wybis.wybisanalytics.service.AutoNumberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service
@Slf4j
public class DefaultAutoNumberService implements AutoNumberService {

    @Resource
    AutoNumberRepository autoNumberRepository

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public synchronized long nextNumber(SessionDto sessionDto, String key) {
        AutoNumber an = null

        an = this.autoNumberRepository.findOne(key)
        if (an == null) {
            an = new AutoNumber()
            an.id = key
            an.createBy = sessionDto.id
            // an.prePersist()
        } else {
            // an.preUpdate()
        }
        an.updateBy = sessionDto.id
        an.value += 1
        an = this.autoNumberRepository.save(an)

        return an.value
    }

}
