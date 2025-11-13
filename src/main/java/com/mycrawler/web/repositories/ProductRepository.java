package com.mycrawler.web.repositories;

import com.mycrawler.web.entities.ProductEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ProductRepository extends ListCrudRepository<ProductEntity, Long> {

    List<ProductEntity> findByOrderByProductNameAsc();

    ProductEntity findByProductName(String productName);
}
