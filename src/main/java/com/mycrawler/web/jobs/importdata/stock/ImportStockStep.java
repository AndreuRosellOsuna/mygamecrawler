package com.mycrawler.web.jobs.importdata.stock;

import com.mycrawler.web.entities.StockEntity;
import com.mycrawler.web.repositories.ProductRepository;
import com.mycrawler.web.repositories.StockRepository;
import com.mycrawler.web.repositories.StoreRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportStockStep {

    @Bean
    public Step importStocks(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             ImportStockProcessor importStockProcessor,
                             JpaItemWriter<StockEntity> stockWriter) {
        return new StepBuilder("importStocks", jobRepository)
                .<StockData, StockEntity>chunk(10, transactionManager)
                .reader(stockReader())
                .processor(importStockProcessor)
                .writer(stockWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<StockData> stockReader() {
        return new FlatFileItemReaderBuilder<StockData>()
                .name("stockReader")
                .resource(new ClassPathResource("data/stocks.csv"))
                .delimited()
                .names("productName", "storeCode", "url")
                .targetType(StockData.class)
                .build();
    }

    @Bean
    public ImportStockProcessor importSockProcessor(StoreRepository storeRepository,
                                                    ProductRepository productRepository,
                                                    StockRepository stockRepository) {
        return new ImportStockProcessor(storeRepository, productRepository, stockRepository);
    }

    @Bean
    public JpaItemWriter<StockEntity> stockWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<StockEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
