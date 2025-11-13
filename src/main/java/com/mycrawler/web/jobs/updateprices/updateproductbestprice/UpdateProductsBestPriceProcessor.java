package com.mycrawler.web.jobs.updateprices.updateproductbestprice;

import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.entities.StockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

public class UpdateProductsBestPriceProcessor implements ItemProcessor<ProductEntity, ProductEntity> {

    private final Logger logger = LoggerFactory.getLogger(UpdateProductsBestPriceProcessor.class);

    @Override
    public ProductEntity process(ProductEntity item) throws Exception {
        logger.info("Updating product best price from item {}", item.getProductName());

        BigDecimal minimumPrice = item.getStocks()
                .stream()
                .map(StockEntity::getPrice)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
        item.setBestPrice(minimumPrice);
        return item;
    }
}
