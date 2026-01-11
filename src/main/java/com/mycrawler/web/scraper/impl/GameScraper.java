package com.mycrawler.web.scraper.impl;

import com.mycrawler.web.scraper.AbstractScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class GameScraper extends AbstractScraper {

    private final Logger logger = LoggerFactory.getLogger(GameScraper.class);

    protected GameScraper(Integer timeout) {
        super(timeout);
    }

    @Override
    public void acceptConsentCookies() {
        if(page.locator("#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll").count() > 0) {
            page.click("#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll");
        }
    }

    @Override
    public BigDecimal getProductPrice(String name) {
        String price = page.locator("#ProductPrice").inputValue();
        logger.info("price for {} is {}", name, price);
        return new BigDecimal(price.replace(",", "."));
    }
}
