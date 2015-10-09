package io.wybis.wybisanalytics.repository

import io.wybis.wybisanalytics.model.AccessData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

public interface AccessDataRepository extends
        PagingAndSortingRepository<AccessData, Long> {

        AccessData findByApplicationCodeAndRequestId(String applicationCode, String requestId)

        Page<AccessData> findByApplicationCodeAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(String applicationCode, Date startTime, Date endTime, Pageable pageable);
}
