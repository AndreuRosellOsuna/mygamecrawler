package com.mycrawler.web.jobs.updateprices.notifysubscribers;

import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.entities.SubscriptionEntity;
import com.mycrawler.web.jobs.updateprices.notifysubscribers.dto.NotifySubscribersOutput;
import com.mycrawler.web.jobs.updateprices.notifysubscribers.dto.NotifySubscribersProduct;
import com.mycrawler.web.repositories.SubscriptionRepository;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class NotifySubscribersProcessor implements ItemProcessor<String, NotifySubscribersOutput> {

    private final SubscriptionRepository subscriptionRepository;

    public NotifySubscribersProcessor(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public NotifySubscribersOutput process(String username) throws Exception {

        List<SubscriptionEntity> subscriptionsByUser = subscriptionRepository.findByUsername(username);
        List<ProductEntity> productsToNotify = subscriptionsByUser.stream()
                .map(SubscriptionEntity::getProduct)
                .filter(productEntity -> productEntity.getBestPrice() != null && productEntity.getBestPrice().compareTo(productEntity.getPreviousBestPrice()) < 0)
                .toList();

        if(!productsToNotify.isEmpty()) {
            List<NotifySubscribersProduct> products = productsToNotify.stream()
                    .map(product -> new NotifySubscribersProduct(product.getProductName(), product.getBestPrice()))
                    .toList();
            return new NotifySubscribersOutput(username, "andreu.rosell4@gmail.com", products);
        };

        return null;
    }
}
