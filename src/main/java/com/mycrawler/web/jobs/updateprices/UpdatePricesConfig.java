package com.mycrawler.web.jobs.updateprices;

import com.mycrawler.web.entities.StoreEntity;
import com.mycrawler.web.jobs.importdata.ImportDataJobCompletedSuccessfully;
import com.mycrawler.web.repositories.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Date;

@Configuration
@EnableAsync
public class UpdatePricesConfig {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePricesConfig.class);

    private final JobLauncher jobLauncher;
    private final StoreRepository storeRepository;
    private Job updatePricesJob;
    private Job updateProductsBestPricesJob;

    public UpdatePricesConfig(JobLauncher jobLauncher, StoreRepository storeRepository) {
        this.jobLauncher = jobLauncher;
        this.storeRepository = storeRepository;
    }

    @EventListener(ImportDataJobCompletedSuccessfully.class)
    @Async
    public void handleApplicationReady(ImportDataJobCompletedSuccessfully event) {
        logger.info("Update stock prices initialized");
        storeRepository.findAll()
                .stream()
                .map(StoreEntity::getStoreCode)
                .forEach(this::runUpdateStockPricesJob);
        logger.info("Update stock prices finished");

        runProductBestPrices();
    }

    @Bean
    public Job updateStockPricesJob(JobRepository jobRepository, Step loadStocksAndUpdatePrice) {
        Job updatePricesJob = new JobBuilder("updateStockPricesJob", jobRepository)
                .start(loadStocksAndUpdatePrice)
                .listener(this)
                .build();
        this.updatePricesJob = updatePricesJob;
        return updatePricesJob;
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            String store = jobExecution.getJobParameters().getString("store");
            logImportDataInfo("Update price from " + store + " store finished successfully");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            logger.error("Update price finished with errors");
        }
    }

    public static void logImportDataInfo(String logging) {
        logger.info("\u001B[34m{}\u001B[0m", logging);
    }

    @Bean
    public Job updateProductsBestPricesJob(JobRepository jobRepository, Step loadProductsAndUpdateBestPrice) {
        Job updateProductsBestPricesJob = new JobBuilder("updateProductsBestPricesJob", jobRepository)
                .start(loadProductsAndUpdateBestPrice)
                .listener(this)
                .build();
        this.updateProductsBestPricesJob = updateProductsBestPricesJob;
        return updateProductsBestPricesJob;
    }

    private void runUpdateStockPricesJob(String store) {
        var jobParameters = new JobParametersBuilder()
                .addString("store", store)
                .addDate("timestamp", new Date())
                .toJobParameters();
        try {
            logger.info("Starting update prices job for store: {}", store);
            jobLauncher.run(updatePricesJob, jobParameters);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
    }

    private void runProductBestPrices() {
        logger.info("Update products best price initialized");
        var jobParameters = new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters();
        try {
            jobLauncher.run(updateProductsBestPricesJob, jobParameters);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
        logger.info("Update products best price finished");
    }
}
