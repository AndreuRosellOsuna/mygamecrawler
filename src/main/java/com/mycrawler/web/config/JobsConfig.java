package com.mycrawler.web.config;

import com.mycrawler.web.jobs.updateprices.UpdatePricesRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableAsync
@EnableScheduling
public class JobsConfig {

    private static final Logger logger = LoggerFactory.getLogger(JobsConfig.class);
    private final UpdatePricesRunner updatePricesRunner;

    public JobsConfig(UpdatePricesRunner updatePricesRunner) {
        this.updatePricesRunner = updatePricesRunner;
    }

    @Scheduled(cron="0 0 8,20 * * *")
    public void scheduleUpdatePricesJob() {
        logger.info("Update stock prices job scheduled");
        updatePricesRunner.runUpdateStockPricesJob();
    }
}
