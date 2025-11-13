package com.mycrawler.web.converters;

import com.mycrawler.web.entities.SubscriptionEntity;
import com.mycrawler.web.views.SubscriptionView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionConverter implements Converter<SubscriptionEntity, SubscriptionView> {

    private final StockConverter stockConverter;

    public SubscriptionConverter(StockConverter stockConverter) {
        this.stockConverter = stockConverter;
    }

    @Override
    public SubscriptionView convert(SubscriptionEntity subscriptionEntity) {
        return new SubscriptionView(
                subscriptionEntity.getProduct().getProductId(),
                subscriptionEntity.getProduct().getProductName(),
                subscriptionEntity.getProduct().getBestPrice(),
                subscriptionEntity.getProduct().getStocks().stream().map(stockConverter::convert).toList()
        );
    }
}
