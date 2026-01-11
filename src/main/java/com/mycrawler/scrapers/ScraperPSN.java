package com.mycrawler.scrapers;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Map;

public class ScraperPSN {

    private static final Map<String, String> games = Map.of(
            "GodOfWar", "https://www.playstation.com/es-es/games/god-of-war-ragnarok/",
            "IndianaJones", "https://www.playstation.com/es-es/games/indiana-jones-and-the-great-circle/"
    );

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            games.forEach((name, url) -> {
                System.out.println("Scraping " + name + " from " + url);
                Page page = browser.newPage();
//                Response res = page.navigate(url);
                page.navigate(url, new Page.NavigateOptions().setTimeout(120000));

                if(page.locator("#onetrust-accept-btn-handler").count() > 0) {
                    page.click("#onetrust-accept-btn-handler");
                }

                String price;
//                int countLocalPriceLocator = page.locator(".buybox__price").count();
                boolean secondaryPriceLocator = page.isVisible("[data-qa=\"mfeCtaMain#offer1#finalPrice\"]");

                if(secondaryPriceLocator) {
                    price = page.locator("[data-qa=\"mfeCtaMain#offer1#finalPrice\"]").textContent().trim();
                } else {
                    price = page.locator("[data-qa=\"mfeCtaMain#offer0#finalPrice\"]").textContent().trim();
                }

                System.out.println("price for " + name + " is " + price);

//                page.screenshot(new Page.ScreenshotOptions()
//                        .setPath(Paths.get("export/psn/" + name + ".png"))
//                        .setFullPage(false));
                page.close();
            });

            browser.close();
        }
    }
}
