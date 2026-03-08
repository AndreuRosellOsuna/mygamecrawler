package com.mycrawler.web.jobs.updateprices.updatestockprice;

import com.mycrawler.web.entities.StockEntity;
import com.mycrawler.web.scraper.ScraperActions;
import com.mycrawler.web.scraper.impl.ScraperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class UpdateStockPriceProcessor implements ItemProcessor<StockEntity, StockEntity> {

    private final Logger logger = LoggerFactory.getLogger(UpdateStockPriceProcessor.class);

    private final String store;
    private final ScraperFactory scraperFactory;

    public UpdateStockPriceProcessor(String store, ScraperFactory scraperFactory) {
        this.store = store;
        this.scraperFactory = scraperFactory;
    }

    @Override
    public StockEntity process(StockEntity item) throws Exception {

        logger.info("Updating stock price item {} from store {}", item.getStockId(), store);

        String productName = item.getProduct().getProductName();

        ScraperActions gameScraper = scraperFactory.getScraper(store);

        try {
            BigDecimal productPrice = gameScraper.runPriceScrap(productName, item.getUrl());
            item.setPrice(productPrice);
        } catch (Exception e) {
            logger.error("Error updating stock price for item {} from store {} - Error: {}", item.getStockId(), store, e.toString());
            item.setPrice(null);
        }
        return item;
    }
}
