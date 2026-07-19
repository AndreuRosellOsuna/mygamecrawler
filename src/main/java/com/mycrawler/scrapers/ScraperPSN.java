package com.mycrawler.scrapers;

import com.mycrawler.web.scraper.ScraperActions;
import com.mycrawler.web.scraper.impl.ScraperFactory;

import java.math.BigDecimal;
import java.util.Map;

public class ScraperPSN {

    private static final Map<String, String> games = Map.of(
            "GodOfWar", "https://www.playstation.com/es-es/games/god-of-war-ragnarok/",
            "IndianaJones", "https://www.playstation.com/es-es/games/indiana-jones-and-the-great-circle/"
    );

    public static void main(String[] args) {
        runScrapper();
    }

    private static void runScrapper() {
        ScraperFactory scraperFactory = new ScraperFactory(10000);
        ScraperActions gameScraper = scraperFactory.getScraper("PSN");

        games.forEach((name, url) -> {
            try {
                BigDecimal productPrice = gameScraper.runPriceScrap(name, url);
                System.out.println("price for " + name + " is " + productPrice);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("error!");
            }
        });

    }
}
