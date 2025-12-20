package com.mycrawler.web.jobs.updateprices.notifysubscribers;

import com.mycrawler.web.jobs.updateprices.notifysubscribers.dto.NotifySubscribersOutput;
import com.mycrawler.web.repositories.SubscriptionRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class NotifySubscribersStep {

    @Bean
    public Step notifySubscribers(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  JpaPagingItemReader<String> findUsersWithSubscriptions,
                                  NotifySubscribersProcessor notifySubscribersProcessor,
                                  NotifySubscribersWriter notifySubscribersWriter) {
        return new StepBuilder("notifySubscribers", jobRepository)
                .<String, NotifySubscribersOutput>chunk(1, transactionManager)
                .reader(findUsersWithSubscriptions)
                .processor(notifySubscribersProcessor)
                .writer(notifySubscribersWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<String> findUsersWithSubscriptions(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<String>()
                .name("findUsersWithSubscriptions")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select distinct s.username from subscription s order by s.username")
                .pageSize(3)
                .build();
    }

    @Bean
    public NotifySubscribersProcessor notifySubscribersProcessor(SubscriptionRepository subscriptionRepository) {
        return new NotifySubscribersProcessor(subscriptionRepository);
    }

    @Bean
    public NotifySubscribersWriter notifySubscribersWriter() {
        return new NotifySubscribersWriter();
    }
}
