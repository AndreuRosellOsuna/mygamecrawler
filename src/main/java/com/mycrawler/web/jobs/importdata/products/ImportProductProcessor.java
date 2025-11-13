package com.mycrawler.web.jobs.importdata.products;

import com.mycrawler.web.entities.ProductEntity;
import com.mycrawler.web.repositories.ProductRepository;
import org.springframework.batch.item.ItemProcessor;

public class ImportProductProcessor implements ItemProcessor<ProductData, ProductEntity> {

    private final ProductRepository productRepository;

    public ImportProductProcessor(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity process(ProductData item) throws Exception {
        ProductEntity existingProduct = productRepository.findByProductName(item.name());
        if(existingProduct != null) return null;

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(item.name());
        productEntity.setProductDescription(item.description());
        return productEntity;
    }
}
