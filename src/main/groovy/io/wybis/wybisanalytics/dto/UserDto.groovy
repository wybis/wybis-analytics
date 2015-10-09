package io.wybis.wybisanalytics.dto

import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString(includeNames = true)
class UserDto implements Serializable {

    String userId

    String password

    String newPassword
}
