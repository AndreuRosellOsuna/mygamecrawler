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

    protected Browser browser;
    protected Page page;

    public BigDecimal runPriceScrap(String name, String url) {
        BigDecimal price;
        logger.info("Scraping {} from {}", name, url);

        try (Playwright playwright = Playwright.create()) {
            logger.info("Playwright created, trying launch browser");

            browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true)
//                    .setArgs(Collections.singletonList("--mute-audio"))
                    .setTimeout(60000));
            page = browser.newPage();

            logger.info("Page created, trying navigate to {}", url);
            page.navigate(url, new Page.NavigateOptions().setTimeout(60000));

            logger.info("Navigated to {}, trying accept consent cookies", url);
            acceptConsentCookies();

            logger.info("Accepted consent cookies, trying get price for {}", name);
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
