package com.mycrawler.scrapers;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ScraperMediamarkt {

    private static final Map<String, String> games = Map.of(
            "IndianaJones", "https://www.mediamarkt.es/es/product/_ps5-indiana-jones-y-el-gran-circulotm-1597165.html",
            "NintendoSwitch", "https://www.mediamarkt.es/es/product/_consola-nintendo-switch-2-79-full-hd-hdr-120-hz-256-gb-magnetic-joy-con-2-con-modo-raton-azul-y-rojo-neon-juego-mario-kart-world-1598254.html",
            "GodOfWar", "https://www.mediamarkt.es/es/product/_ps5-god-of-war-ragnarok-1536839.html"
    );

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            games.forEach((name, url) -> {
                Page page = browser.newPage();
                Response res = page.navigate(url);
                page.click("#pwa-consent-layer-accept-all-button");

                String price;

                List<Locator> spans = page.locator("[data-test=\"mms-product-price\"]").locator("span").all();
                System.out.println("span size is " + spans.size());
                if(spans.size() >= 6) {
                    price = spans.get(7).textContent();
                } else {
                    price = spans.get(3).textContent();
                }

                System.out.println("price for " + name + " is " + price);

//                page.screenshot(new Page.ScreenshotOptions()
//                        .setPath(Paths.get("export/mediamarkt/" + name + ".png"))
//                        .setFullPage(true));
                page.close();
            });

            browser.close();
        }
    }
}
