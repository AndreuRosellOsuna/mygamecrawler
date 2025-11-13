package com.mycrawler.web.service;

import com.mycrawler.web.converters.SubscriptionConverter;
import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.entities.SubscriptionEntity;
import com.mycrawler.web.repositories.SubscriptionRepository;
import com.mycrawler.web.views.SubscriptionView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionConverter subscriptionConverter;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionConverter subscriptionConverter) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionConverter = subscriptionConverter;
    }

    public List<SubscriptionView> getSubscriptions(String username) {
        var userSubscriptions = subscriptionRepository.findByUsername(username);
        return userSubscriptions.stream().map(subscriptionConverter::convert).toList();
    }

    public List<Long> getSubscriptionsProductIdList(String username) {
        return subscriptionRepository.findByUsername(username)
                .stream()
                .map(SubscriptionEntity::getProduct)
                .map(ProductEntity::getProductId)
                .sorted()
                .toList();
    }

    public void subscribe(String username, Long productId) {
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setUsername(username);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productId);
        subscriptionEntity.setProduct(productEntity);

        subscriptionRepository.save(subscriptionEntity);
    }

    public SubscriptionEntity findByUsernameAndProductProductId(String username, Long productId) {
        return subscriptionRepository.findByUsernameAndProductProductId(username, productId);
    }

    public void unsubscribe(String username, Long productId) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUsernameAndProductProductId(username, productId);
        subscriptionRepository.delete(subscriptionEntity);
    }
}
