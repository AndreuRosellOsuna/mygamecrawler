package com.mycrawler.web.scraper.impl;

import com.mycrawler.web.scraper.AbstractScraper;
import com.mycrawler.web.scraper.PriceExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class PSNScraper extends AbstractScraper {

    private final Logger logger = LoggerFactory.getLogger(PSNScraper.class);

    protected PSNScraper(Integer timeout) {
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
        boolean secondaryPriceLocator = page.isVisible("[data-qa=\"mfeCtaMain#offer1#finalPrice\"]");

        if(secondaryPriceLocator) {
            price = page.locator("[data-qa=\"mfeCtaMain#offer1#finalPrice\"]").textContent().trim();
        } else {
            price = page.locator("[data-qa=\"mfeCtaMain#offer0#finalPrice\"]").textContent().trim();
        }
        logger.info("price for {} is {}", name, price);

        String fixedPrice = PriceExtractor.validateBigDecimalFromString(price);
        logger.info("fixed price for {} is \"{}\"", name, fixedPrice);

        assert fixedPrice != null;
        return new BigDecimal(fixedPrice);
    }
}
