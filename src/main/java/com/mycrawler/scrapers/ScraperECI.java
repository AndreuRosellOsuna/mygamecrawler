package com.mycrawler.scrapers;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Map;

public class ScraperECI {

    private static final Map<String, String> games = Map.of(
            "IndianaJones", "https://www.elcorteingles.es/videojuegos/A55341263-indiana-jones-y-el-gran-circulo-playstation-5/",
            "HorizonZeroDawn", "https://www.elcorteingles.es/videojuegos/A53190084-juego-para-playstation-5-horizon-zero-dawn-remasterizado/",
            "GodOfWar", "https://www.elcorteingles.es/videojuegos/A44473002-god-of-war-ragnarok-edicion-estandar-playstation-5/",
            "MetalGearSolid", "https://www.elcorteingles.es/videojuegos/A52039132-metal-gear-solid-snake-eater-day-one-edition-playstation-5/"
    );

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            games.forEach((name, url) -> {
                Page page = browser.newPage();
                Response res = page.navigate(url);
                page.click("#onetrust-accept-btn-handler");
                String price = page.locator(".product-detail-price").textContent();
                System.out.println("price for " + name + " is " + price);

//                page.screenshot(new Page.ScreenshotOptions()
//                        .setPath(Paths.get("export/eci/" + name + ".png"))
//                        .setFullPage(true));
                page.close();
            });

            browser.close();
        }
    }
}
