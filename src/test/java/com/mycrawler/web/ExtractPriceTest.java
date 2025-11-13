package com.mycrawler.web;

import com.mycrawler.web.scraper.PriceExtractor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class ExtractPriceTest {
    
    @Test
    void shouldExtractBigDecimal() {
        List<String> prices = List.of(
                " 123,03 € ", 
                " 123,03 €", 
                "123,03 €", 
                " 123,03€", 
                " 123,03\u00a0€",
                " 123,03 ",
                "123,03 ",
                " 123,03",
                "123,03"
        );
        
        prices.forEach(price -> {
            BigDecimal bigDecimal = PriceExtractor.extractBigDecimalFromString(price);
            System.out.println(bigDecimal);
        });
    }
}
