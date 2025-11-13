package com.mycrawler.web.jobs.importdata.stores;

import com.mycrawler.web.entities.StoreEntity;
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
public class ImportStoresStep {

    @Bean
    public Step importStores(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             JpaItemWriter<StoreEntity> storesWriter,
                             StoreRepository storeRepository) {
        return new StepBuilder("importStores", jobRepository)
                .<StoresData, StoreEntity>chunk(10, transactionManager)
                .reader(storesReader())
                .processor(importStoresProcessor(storeRepository))
                .writer(storesWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<StoresData> storesReader() {
        return new FlatFileItemReaderBuilder<StoresData>()
                .name("storesReader")
                .resource(new ClassPathResource("data/stores.csv"))
                .delimited()
                .names("code", "name")
                .targetType(StoresData.class)
                .build();
    }

    @Bean
    public ImportStoresProcessor importStoresProcessor(StoreRepository storeRepository) {
        return new ImportStoresProcessor(storeRepository);
    }

    @Bean
    public JpaItemWriter<StoreEntity> storesWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<StoreEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
}
