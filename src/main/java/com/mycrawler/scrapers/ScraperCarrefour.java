package com.mycrawler.scrapers;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Map;

public class ScraperCarrefour {

    private static final Map<String, String> games = Map.of(
            "GodOfWar", "https://www.carrefour.es/god-of-war-ragnarok-para-ps5/VC4A-19120929/p",
            "IndianaJones", "https://www.carrefour.es/indiana-jones-y-el-gran-circulo-para-ps5/VC4A-30252337/p",
            "TheLastOfUs", "https://www.carrefour.es/the-last-of-us-parte-ii-remastered-para-ps5/VC4A-25326099/p"
    );

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            games.forEach((name, url) -> {
                System.out.println("Scraping " + name + " from " + url);
                Page page = browser.newPage();
                Response res = page.navigate(url);
                page.click("#onetrust-accept-btn-handler");

                String price;
//                int countLocalPriceLocator = page.locator(".buybox__price").count();
                boolean isCountDiscountPriceLocator = page.isVisible("css=.buybox__price--current");

                if(isCountDiscountPriceLocator) {
                    price = page.locator("css=.buybox__price--current").textContent().trim();
                } else {
                    price = page.locator("css=.buybox__price").textContent().trim();
                }

                System.out.println("price for " + name + " is " + price);

//                page.screenshot(new Page.ScreenshotOptions()
//                        .setPath(Paths.get("export/carrefour/" + name + ".png"))
//                        .setFullPage(true));
                page.close();
            });

            browser.close();
        }
    }
}
