package io.wybis.wybisanalytics.repository

import io.wybis.wybisanalytics.model.AutoNumber
import org.springframework.data.jpa.repository.JpaRepository

public interface AutoNumberRepository extends JpaRepository<AutoNumber, String> {

}
