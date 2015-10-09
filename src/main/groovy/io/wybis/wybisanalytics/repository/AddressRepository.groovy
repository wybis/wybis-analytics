package io.wybis.wybisanalytics.repository

import io.wybis.wybisanalytics.model.Address
import org.springframework.data.repository.PagingAndSortingRepository

public interface AddressRepository extends
        PagingAndSortingRepository<Address, Long> {

}
