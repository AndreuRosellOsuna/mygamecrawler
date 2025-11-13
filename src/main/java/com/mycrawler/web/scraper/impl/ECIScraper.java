package com.mycrawler.web.scraper.impl;

import com.mycrawler.web.scraper.AbstractScraper;
import com.mycrawler.web.scraper.PriceExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ECIScraper extends AbstractScraper {

    private final Logger logger = LoggerFactory.getLogger(ECIScraper.class);

    @Override
    public void acceptConsentCookies() {
        page.click("#onetrust-accept-btn-handler");
    }

    @Override
    public BigDecimal getProductPrice(String name) {
        String price = page.locator(".product-detail-price").textContent();
        logger.info("price for {} is {}", name, price);

        String fixedPrice = PriceExtractor.validateBigDecimalFromString(price);
        logger.info("fixed price for {} is \"{}\"", name, fixedPrice);

        assert fixedPrice != null;
        return new BigDecimal(fixedPrice);
    }
}
