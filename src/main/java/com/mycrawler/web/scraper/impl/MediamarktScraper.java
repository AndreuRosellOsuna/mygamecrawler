package com.mycrawler.web.scraper.impl;

import com.microsoft.playwright.Locator;
import com.mycrawler.web.scraper.AbstractScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class MediamarktScraper extends AbstractScraper {

    private final Logger logger = LoggerFactory.getLogger(MediamarktScraper.class);

    protected MediamarktScraper(Integer timeout) {
        super(timeout);
    }

    @Override
    public void acceptConsentCookies() {
        page.click("#pwa-consent-layer-accept-all-button");
    }

    @Override
    public BigDecimal getProductPrice(String name) {

        List<Locator> spans = page.locator("[data-test=\"mms-product-price\"]").locator("span").all();
        System.out.println("span size is " + spans.size());
        String price = spans.get(4).textContent();

        logger.info("price for {} is {}", name, price);
        return new BigDecimal(price.replace(",", ".").replace("€", ""));
    }
}
