package io.wybis.wybisanalytics.service

import io.wybis.wybisanalytics.dto.SessionDto

public interface AutoNumberService {

    long nextNumber(SessionDto sessionUser, String key);

}
