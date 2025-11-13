package com.mycrawler.web.views;

import java.math.BigDecimal;
import java.util.List;

public record SubscriptionView(
        Long productId,
        String productName,
        BigDecimal bestPrice,
        List<StockView> stores
) {
}
