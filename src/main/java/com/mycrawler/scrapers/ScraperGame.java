package com.mycrawler.scrapers;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Map;

public class ScraperGame {

    private static final Map<String, String> games = Map.of(
            "IndianaJones", "https://www.game.es/VIDEOJUEGOS/AVENTURA/PLAYSTATION-5/INDIANA-JONES-AND-THE-GREAT-CIRCLE/240393",
            "TheLastOfUs", "https://www.game.es/VIDEOJUEGOS/AVENTURA/PLAYSTATION-5/THE-LAST-OF-US-PARTE-II-REMASTERED/226221",
            "GodOfWar", "https://www.game.es/VIDEOJUEGOS/AVENTURA/PLAYSTATION-5/GOD-OF-WAR-RAGNAR%C3%96K/206644"
    );

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            games.forEach((name, url) -> {
                Page page = browser.newPage();
                Response res = page.navigate(url);
                page.click("#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll");
                String price = page.locator("#ProductPrice").inputValue();
                System.out.println("price for " + name + " is " + price);

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("export/game/" + name + ".png"))
                        .setFullPage(true));
                page.close();
            });

            browser.close();
        }
    }
}
