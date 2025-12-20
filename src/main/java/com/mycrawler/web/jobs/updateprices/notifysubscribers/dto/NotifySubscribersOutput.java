package com.mycrawler.web.jobs.updateprices.notifysubscribers.dto;

import java.util.List;

public record NotifySubscribersOutput(
        String name,
        String email,
        List<NotifySubscribersProduct> products
        ) {
}
