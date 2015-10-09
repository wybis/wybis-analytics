package io.wybis.wybisanalytics.repository

import io.wybis.wybisanalytics.model.BlobData
import org.springframework.data.repository.PagingAndSortingRepository

public interface BlobDataRepository extends
        PagingAndSortingRepository<BlobData, Long> {

}
