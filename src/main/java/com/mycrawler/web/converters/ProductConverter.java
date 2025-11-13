package com.mycrawler.web.converters;

import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.views.ProductView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProductConverter implements Converter<ProductEntity, ProductView> {

    private final StockConverter stockConverter;

    public ProductConverter(StockConverter stockConverter) {
        this.stockConverter = stockConverter;
    }

    public ProductView convert(ProductEntity product, List<Long> userSubscriptionsProductIdList) {
        return new ProductView(
                product.getProductId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getStocks().stream()
                        .map(stockConverter::convert)
                        .filter(Objects::nonNull)
                        .sorted()
                        .toList(),
                product.getBestPrice(),
                userSubscriptionsProductIdList.contains(product.getProductId()));
    }

    @Override
    public ProductView convert(ProductEntity product) {
        return convert(product, null);
    }
}
