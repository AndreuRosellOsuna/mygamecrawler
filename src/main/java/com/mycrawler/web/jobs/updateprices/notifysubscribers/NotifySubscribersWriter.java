package com.mycrawler.web.jobs.updateprices.notifysubscribers;

import com.mycrawler.web.jobs.updateprices.notifysubscribers.dto.NotifySubscribersOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class NotifySubscribersWriter implements ItemWriter<NotifySubscribersOutput> {

    private static final Logger logger = LoggerFactory.getLogger(NotifySubscribersWriter.class);

    @Override
    public void write(Chunk<? extends NotifySubscribersOutput> chunk) throws Exception {
        List<? extends NotifySubscribersOutput> items = chunk.getItems();
        items.forEach(userNotification -> {
            logger.info("Notifying user {} with email {}", userNotification.name(), userNotification.email());

            userNotification.products().forEach(product -> {
                logger.info("Product {} has price {}", product.name(), product.price() + "");
            });
        });
    }
}
