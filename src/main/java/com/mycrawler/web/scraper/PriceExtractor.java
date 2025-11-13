package com.mycrawler.web.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class PriceExtractor {

    private final static Logger logger = LoggerFactory.getLogger(PriceExtractor.class);

    public static BigDecimal extractBigDecimalFromString(String priceInString) {
        return new BigDecimal(extractStringPrice(priceInString));
    }

    public static String validateBigDecimalFromString(String priceInString) {
        String price = extractStringPrice(priceInString);
        try {
            new BigDecimal(price);
        } catch (NumberFormatException e) {
            logger.error("Invalid price format: {}", price);
            return null;
        }
        return price;
    }

    private static String extractStringPrice(String priceInString) {
        String[] splits = priceInString
                .replace(",", ".")
                .replace("€", "")
                .replace("\u00a0","")
                .trim()
                .split(" ");
        Assert.isTrue(splits.length >= 1, "Price should contain at least 1 element");
        return splits[0];
    }
}
