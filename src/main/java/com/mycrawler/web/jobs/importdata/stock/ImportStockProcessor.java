package com.mycrawler.web.jobs.importdata.stock;

import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.entities.StockEntity;
import com.mycrawler.web.entities.StoreEntity;
import com.mycrawler.web.repositories.ProductRepository;
import com.mycrawler.web.repositories.StockRepository;
import com.mycrawler.web.repositories.StoreRepository;
import org.springframework.batch.item.ItemProcessor;

public class ImportStockProcessor implements ItemProcessor<StockData, StockEntity> {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ImportStockProcessor(StoreRepository storeRepository, ProductRepository productRepository, StockRepository stockRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public StockEntity process(StockData item) throws Exception {
        StockEntity existingStock = stockRepository.findByUrl(item.url());
        if(existingStock != null) return null;

        StoreEntity store = storeRepository.findByStoreCode(item.storeCode());
        ProductEntity product = productRepository.findByProductName(item.productName());

        if(store == null || product == null) return null;

        StockEntity storeProductEntity = new StockEntity();
        storeProductEntity.setProduct(product);
        storeProductEntity.setStore(store);
        storeProductEntity.setUrl(item.url());

        return storeProductEntity;
    }
}
