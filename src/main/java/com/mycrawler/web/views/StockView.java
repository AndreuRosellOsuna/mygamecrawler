package com.mycrawler.web.views;

import java.math.BigDecimal;

public record StockView(
        String storeCode,
        String storeName,
        String url,
        BigDecimal price
) implements Comparable<StockView>{

    @Override
    public int compareTo(StockView o) {
        return this.storeName.compareTo(o.storeName);
    }
}
