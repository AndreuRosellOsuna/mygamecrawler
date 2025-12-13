package com.mycrawler.web.jobs.updateprices.updateproductbestprice;

import com.mycrawler.web.entities.ProductEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class UpdateProductBestPriceStep {

    @Bean
    public Step loadProductsAndUpdateBestPrice(JobRepository jobRepository,
                                               PlatformTransactionManager transactionManager,
                                               JpaPagingItemReader<ProductEntity> readProducts,
                                               UpdateProductsBestPriceProcessor updateProductsBestPriceProcessor,
                                               JpaItemWriter<ProductEntity> updateProductsBestPriceWriter) {
        return new StepBuilder("loadProductsAndUpdateBestPrice", jobRepository)
                .<ProductEntity, ProductEntity>chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .reader(readProducts)
                .processor(updateProductsBestPriceProcessor)
                .writer(updateProductsBestPriceWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<ProductEntity> readProducts(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<ProductEntity>()
                .name("readProducts")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from product p")
                .pageSize(3)
                .build();
    }

    @Bean
    public UpdateProductsBestPriceProcessor updateProductsBestPriceProcessor() {
        return new UpdateProductsBestPriceProcessor();
    }

    @Bean
    public JpaItemWriter<ProductEntity> updateProductsBestPriceWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<ProductEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
