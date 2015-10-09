package io.wybis.wybisanalytics.service.impl

import io.wybis.wybisanalytics.service.AutoNumberService

import javax.annotation.Resource

public abstract class AbstractService {

    @Resource
    protected AutoNumberService autoNumberService;

}
