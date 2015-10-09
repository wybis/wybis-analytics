package io.wybis.wybisanalytics.repository

import io.wybis.wybisanalytics.model.User
import org.springframework.data.jpa.repository.JpaRepository

public interface UserRepository extends JpaRepository<User, String> {

    User findByUserId(String userId)
}
