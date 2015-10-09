package io.wybis.wybisanalytics.repository

import io.wybis.wybisanalytics.model.Country
import org.springframework.data.jpa.repository.JpaRepository

public interface CountryRepository extends JpaRepository<Country, String> {

}
