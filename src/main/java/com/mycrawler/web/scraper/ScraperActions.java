package com.mycrawler.web.scraper;

import java.math.BigDecimal;

public interface ScraperActions {

    BigDecimal runPriceScrap(String name, String url);

    void saveScreenShot(String name);
}
