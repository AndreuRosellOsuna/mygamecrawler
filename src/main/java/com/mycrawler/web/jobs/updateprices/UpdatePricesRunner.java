package com.mycrawler.web.jobs.updateprices;

import com.mycrawler.web.entities.StoreEntity;
import com.mycrawler.web.jobs.importdata.ImportDataJobCompletedSuccessfully;
import com.mycrawler.web.repositories.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

@Configuration
public class UpdatePricesRunner {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePricesRunner.class);

    private final JobLauncher jobLauncher;
    private final StoreRepository storeRepository;
    private final Job updateStockPricesJob;
    private final Job updateProductsBestPricesJob;
    private final Job notifySubscribersJob;

    public UpdatePricesRunner(JobLauncher jobLauncher, StoreRepository storeRepository, Job updateStockPricesJob, Job updateProductsBestPricesJob, Job notifySubscribersJob) {
        this.jobLauncher = jobLauncher;
        this.storeRepository = storeRepository;
        this.updateStockPricesJob = updateStockPricesJob;
        this.updateProductsBestPricesJob = updateProductsBestPricesJob;
        this.notifySubscribersJob = notifySubscribersJob;
    }

    @EventListener(ImportDataJobCompletedSuccessfully.class)
    @Async
    public void runUpdatePricesJobs(ImportDataJobCompletedSuccessfully event) {
        runUpdateStockPricesJob();
        runProductBestPrices();
        runNotifySubscribers();
    }

    private void runUpdateStockPricesJob() {
        logger.info("Update stock prices initialized");
        storeRepository.findAll()
                .stream()
                .map(StoreEntity::getStoreCode)
                .forEach(this::runUpdateStockPricesJob);
        logger.info("Update stock prices finished");
    }

    private void runUpdateStockPricesJob(String store) {
        var jobParameters = new JobParametersBuilder()
                .addString("store", store)
                .addDate("timestamp", new Date())
                .toJobParameters();
        try {
            logger.info("Starting update prices job for store: {}", store);
            jobLauncher.run(updateStockPricesJob, jobParameters);
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

    private void runNotifySubscribers() {
        logger.info("Notify subscribers initialized");
        var jobParameters = new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters();
        try {
            jobLauncher.run(notifySubscribersJob, jobParameters);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
        logger.info("Notify subscribers finished");
    }
}
