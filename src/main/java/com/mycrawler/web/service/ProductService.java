package com.mycrawler.web.service;

import com.mycrawler.web.converters.ProductConverter;
import com.mycrawler.web.repositories.ProductRepository;
import com.mycrawler.web.views.ProductView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final SubscriptionService subscriptionService;

    public ProductService(ProductRepository productRepository, ProductConverter productConverter, SubscriptionService subscriptionService) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.subscriptionService = subscriptionService;
    }

    public List<ProductView> getProducts(String username) {
        var userSubscriptionsProductIdList = subscriptionService.getSubscriptionsProductIdList(username);
        return productRepository.findByOrderByProductNameAsc()
                .stream()
                .map(product -> productConverter.convert(product, userSubscriptionsProductIdList))
                .toList();
    }

    public void subscribeToProduct(String username, Long productId) {
        var subscription = subscriptionService.findByUsernameAndProductProductId(username, productId);
        if(subscription != null) {
            throw new IllegalArgumentException("Subscription already found");
        };
        subscriptionService.subscribe(username, productId);
    }

    public void unsubscribeToProduct(String username, Long productId) {
        var subscription = subscriptionService.findByUsernameAndProductProductId(username, productId);
        if(subscription == null) {
            throw new IllegalArgumentException("Subscription not found");
        };
        subscriptionService.unsubscribe(username, productId);
    }
}
