package com.mycrawler.web.scraper.impl;

import com.mycrawler.web.scraper.ScraperActions;
import org.springframework.stereotype.Component;

@Component
public class ScraperFactory {

    public ScraperActions getScraper(String name) {
        return switch (name) {
            case "CARREFOUR" -> new CarrefourScraper();
            case "ECI" -> new ECIScraper();
            case "GAME" -> new GameScraper();
            case "MEDIAMARKT" -> new MediamarktScraper();
            case "PSN" -> new PSNScraper();
            default -> throw new IllegalArgumentException("Unknown scraper name: " + name);
        };
    }
}
