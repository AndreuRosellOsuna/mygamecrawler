package com.mycrawler.web.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public abstract class AbstractScraper implements ScraperActions {

    private final Logger logger = LoggerFactory.getLogger(AbstractScraper.class);

    private final Integer timeout;

    protected Browser browser;
    protected Page page;

    protected AbstractScraper(Integer timeout) {
        this.timeout = timeout;
    }

    public BigDecimal runPriceScrap(String name, String url) {
        BigDecimal price;
        logger.info("Scraping {} from {}", name, url);

        try (Playwright playwright = Playwright.create()) {
            logger.debug("Playwright created, trying launch browser");

            browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true)
//                    .setArgs(Collections.singletonList("--mute-audio"))
                    .setTimeout(timeout));
            page = browser.newPage();

            logger.debug("Page created, trying navigate");
            page.navigate(url, new Page.NavigateOptions().setTimeout(timeout));

            logger.debug("Navigated, trying accept consent cookies");
            acceptConsentCookies();

            logger.debug("Accepted consent cookies, trying get price");
            price = getProductPrice(name);

            logger.info("Price for {} is \"{}\"", name, price);

            page.close();
            browser.close();
        }
        return price;
    }

    protected abstract void acceptConsentCookies();

    protected abstract BigDecimal getProductPrice(String name);

    @Override
    public void saveScreenShot(String name) {
//        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("export/eci/" + name + ".png")).setFullPage(true));
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
