package com.mycrawler.web.jobs.importdata.products;

import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.repositories.ProductRepository;
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
public class ImportProductsStep {

    @Bean
    public Step importProducts(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               JpaItemWriter<ProductEntity> productsWriter,
                               ProductRepository productRepository) {
        return new StepBuilder("importProducts", jobRepository)
                .<ProductData, ProductEntity>chunk(10, transactionManager)
                .reader(productsReader())
                .processor(importProductProcessor(productRepository))
                .writer(productsWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<ProductData> productsReader() {
        return new FlatFileItemReaderBuilder<ProductData>()
                .name("productsReader")
                .resource(new ClassPathResource("data/products.csv"))
                .delimited()
                .names("name", "description")
                .targetType(ProductData.class)
                .build();
    }

    @Bean
    public ImportProductProcessor importProductProcessor(ProductRepository productRepository) {
        return new ImportProductProcessor(productRepository);
    }

    @Bean
    public JpaItemWriter<ProductEntity> productsWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<ProductEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
