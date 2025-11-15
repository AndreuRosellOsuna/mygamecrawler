package com.mycrawler.web.jobs.importdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Date;

@Configuration
public class ImportDataJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(ImportDataJobConfig.class);

    private final JobLauncher jobLauncher;
    private final ApplicationEventPublisher applicationEventPublisher;

    private Job importStoresJob;
    private final Boolean importdata;

    public ImportDataJobConfig(JobLauncher jobLauncher, ApplicationEventPublisher applicationEventPublisher,
                               @Value("${mycrawler.importdata:false}") Boolean importdata) {
        this.jobLauncher = jobLauncher;
        this.applicationEventPublisher = applicationEventPublisher;
        this.importdata = importdata;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void handleApplicationReady(ApplicationReadyEvent event) {
        if (!importdata) {
            logger.info("Import data disabled");
            return;
        }
        var jobParameters = new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters();

        try {
            jobLauncher.run(importStoresJob, jobParameters);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
    }

    @Bean
    public Job importDataJob(JobRepository jobRepository,
                             Step importStores,
                             Step importProducts,
                             Step importStocks) {
        Job importDataJob = new JobBuilder("importDataJob", jobRepository)
                .start(importStores)
                .next(importProducts)
                .next(importStocks)
                .listener(this)
                .build();
        this.importStoresJob = importDataJob;
        return importDataJob;
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logImportDataInfo("Import data finished successfully");
            applicationEventPublisher.publishEvent(new ImportDataJobCompletedSuccessfully(jobExecution));
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            logger.error("Import data finished with errors");
        }
    }

    public static void logImportDataInfo(String logging) {
        logger.info("\u001B[36m{}\u001B[0m", logging);
    }
}
