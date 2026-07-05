package com.mycrawler.scrapers;

import com.mycrawler.web.scraper.ScraperActions;
import com.mycrawler.web.scraper.impl.ScraperFactory;

import java.math.BigDecimal;
import java.util.Map;

public class ScraperECI {

    private static final Map<String, String> games = Map.of(
            "IndianaJones", "https://www.elcorteingles.es/videojuegos/A55341263-indiana-jones-y-el-gran-circulo-playstation-5/",
            "HorizonZeroDawn", "https://www.elcorteingles.es/videojuegos/A53190084-juego-para-playstation-5-horizon-zero-dawn-remasterizado/",
            "GodOfWar", "https://www.elcorteingles.es/videojuegos/A44473002-god-of-war-ragnarok-edicion-estandar-playstation-5/",
            "MetalGearSolid", "https://www.elcorteingles.es/videojuegos/A52039132-metal-gear-solid-snake-eater-day-one-edition-playstation-5/"
    );

    public static void main(String[] args) {
        runScrapper();
    }

    private static void runScrapper() {
        ScraperFactory scraperFactory = new ScraperFactory(10000);
        ScraperActions gameScraper = scraperFactory.getScraper("ECI");

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
