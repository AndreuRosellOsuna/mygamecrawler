package com.mycrawler.web.jobs.importdata;

import org.springframework.context.ApplicationEvent;

public class ImportDataJobCompletedSuccessfully extends ApplicationEvent {

    public ImportDataJobCompletedSuccessfully(Object source) {
        super(source);
    }
}
