package com.mycrawler.web.jobs.updateprices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdatePricesConfig {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePricesConfig.class);

    @Bean
    public Job updateStockPricesJob(JobRepository jobRepository, Step loadStocksAndUpdatePrice) {
        return new JobBuilder("updateStockPricesJob", jobRepository)
                .start(loadStocksAndUpdatePrice)
                .listener(this)
                .build();
    }

    @Bean
    public Job updateProductsBestPricesJob(JobRepository jobRepository, Step loadProductsAndUpdateBestPrice) {
        return new JobBuilder("updateProductsBestPricesJob", jobRepository)
                .start(loadProductsAndUpdateBestPrice)
                .listener(this)
                .build();
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            String name = jobExecution.getJobInstance().getJobName();
            String store = jobExecution.getJobParameters().getString("store");
            logJobFinishedSuccessfully(name, store);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            logger.error("Update price finished with errors");
        }
    }

    public static void logJobFinishedSuccessfully(String jobName, String storeCode) {
        var message = jobName + "store finished successfully";
        if (storeCode != null) {
            message += " for store " + storeCode;
        }
        logger.info("\u001B[34m{}\u001B[0m", message);
    }
}
