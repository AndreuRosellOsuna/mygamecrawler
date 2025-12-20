package com.mycrawler.web.jobs.updateprices.notifysubscribers.dto;

import java.math.BigDecimal;

public record NotifySubscribersProduct(
        String name,
        BigDecimal price
) {
}
