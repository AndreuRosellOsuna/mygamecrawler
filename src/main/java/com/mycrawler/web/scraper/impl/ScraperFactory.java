package com.mycrawler.web.scraper.impl;

import com.mycrawler.web.scraper.ScraperActions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScraperFactory {

    private final Integer timeout;

    public ScraperFactory(@Value("${mycrawler.scraper.timeout}") Integer timeout) {
        this.timeout = timeout;
    }

    public ScraperActions getScraper(String name) {
        return switch (name) {
            case "CARREFOUR" -> new CarrefourScraper(timeout);
            case "ECI" -> new ECIScraper(timeout);
            case "GAME" -> new GameScraper(timeout);
            case "MEDIAMARKT" -> new MediamarktScraper(timeout);
            case "PSN" -> new PSNScraper(timeout);
            default -> throw new IllegalArgumentException("Unknown scraper name: " + name);
        };
    }
}
