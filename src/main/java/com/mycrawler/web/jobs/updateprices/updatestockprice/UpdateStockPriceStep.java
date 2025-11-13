package com.mycrawler.web.jobs.updateprices.updatestockprice;

import com.mycrawler.web.entities.StockEntity;
import com.mycrawler.web.scraper.impl.ScraperFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class UpdateStockPriceStep {

    @Bean
    public Step loadStocksAndUpdatePrice(JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager,
                                         EntityManagerFactory entityManagerFactory,
                                         UpdateStockPriceProcessor updateStockPriceProcessor,
                                         JpaPagingItemReader<StockEntity> readStockItems) {
        return new StepBuilder("loadProductAndUpdatePrice", jobRepository)
                .<StockEntity, StockEntity>chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .reader(readStockItems)
                .processor(updateStockPriceProcessor)
                .writer(updateStock(entityManagerFactory))
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<StockEntity> readStockItems(EntityManagerFactory entityManagerFactory,
                                                           @Value("#{jobParameters[store]}") String store) {
        return new JpaPagingItemReaderBuilder<StockEntity>()
                .name("readStockItems")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select sp from stock sp where sp.store.storeCode = '" + store + "'")
                .pageSize(3)
                .build();
    }

    @Bean
    @StepScope
    public UpdateStockPriceProcessor updatePricesProcessor(@Value("#{jobParameters[store]}") String store, ScraperFactory scraperFactory) {
        return new UpdateStockPriceProcessor(store, scraperFactory);
    }

    @Bean
    public JpaItemWriter<StockEntity> updateStock(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<StockEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
