package com.mycrawler.web.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long productId;

    private String productName;

    private String productDescription;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<StockEntity> stocks;

    private BigDecimal bestPrice;

    private BigDecimal previousBestPrice;

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Set<StockEntity> getStocks() {
        return stocks;
    }

    public void setStocks(Set<StockEntity> stocks) {
        this.stocks = stocks;
    }

    public BigDecimal getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(BigDecimal bestPrice) {
        this.bestPrice = bestPrice;
    }

    public BigDecimal getPreviousBestPrice() {
        return previousBestPrice;
    }

    public void setPreviousBestPrice(BigDecimal previousBestPrice) {
        this.previousBestPrice = previousBestPrice;
    }
}
