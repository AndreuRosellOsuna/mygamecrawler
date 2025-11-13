package com.mycrawler.web.repositories;

import com.mycrawler.web.entities.SubscriptionEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SubscriptionRepository extends ListCrudRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findByUsername(String username);
    SubscriptionEntity findByUsernameAndProductProductId(String username, Long productId);
}
