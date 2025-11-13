package com.mycrawler.web.views;

import java.math.BigDecimal;
import java.util.List;

public record ProductView(
        Long id,
        String name,
        String description,
        List<StockView> stocks,
        BigDecimal bestPrice,
        Boolean subscribed) {
}
