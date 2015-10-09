package io.wybis.wybisanalytics.service

import io.wybis.wybisanalytics.model.AccessData

interface AccessDataService {

    boolean hasAccessData(String line)

    AccessData create(String applicationCode, String line)

    void save(AccessData accessData)

    void parseAndCreateAndSaveFrom(File file)
}
