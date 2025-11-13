package com.mycrawler.scrapers;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Map;

public class TesterScraper {

    private final String name = "GodOfWar";
    private final String url = "https://www.game.es/VIDEOJUEGOS/AVENTURA/PLAYSTATION-5/GOD-OF-WAR-RAGNAR%C3%96K/206644";
    private Playwright playwright = null;
    private Browser browser = null;

    public void init() {
        try {
            playwright = Playwright.create();
            browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );
        }  catch (Exception e) {
            System.err.println(e);
        }
    }

    public void run() {
        try {
            Page page = browser.newPage();
            Response res = page.navigate(url);
            page.click("#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll");
            String price = page.locator("#ProductPrice").inputValue();
            System.out.println("price for " + name + " is " + price);

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("export/test/" + name + ".png"))
                    .setFullPage(true));
            page.close();
        }  catch (Exception e) {
            System.err.println(e);
        }
    }

    public void close() {
        playwright.close();
    }

    public static void main(String[] args) {
        TesterScraper scraper = new TesterScraper();
        System.out.println("init");
        scraper.init();
        System.out.println("run");
        scraper.run();
        scraper.close();
        System.out.println("done");
    }
}
