package com.mycrawler.web.scraper.impl;

import com.mycrawler.web.scraper.AbstractScraper;
import com.mycrawler.web.scraper.PriceExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class CarrefourScraper extends AbstractScraper {

    private final Logger logger = LoggerFactory.getLogger(CarrefourScraper.class);

    protected CarrefourScraper(Integer timeout) {
        super(timeout);
    }

    @Override
    public void acceptConsentCookies() {
        if(page.locator("#onetrust-accept-btn-handler").count() > 0) {
            page.click("#onetrust-accept-btn-handler");
        }
    }

    @Override
    public BigDecimal getProductPrice(String name) {
        String price;
        boolean isCountDiscountPriceLocator = page.isVisible("css=.buybox__price--current");

        if(isCountDiscountPriceLocator) {
            price = page.locator("css=.buybox__price--current").textContent();
        } else {
            price = page.locator("css=.buybox__price").textContent();
        }

        logger.info("price for {} is \"{}\"", name, price);

        String fixedPrice = PriceExtractor.validateBigDecimalFromString(price);
        logger.info("fixed price for {} is \"{}\"", name, fixedPrice);

        assert fixedPrice != null;
        return new BigDecimal(fixedPrice);
    }
}
