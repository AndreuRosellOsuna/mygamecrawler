package com.mycrawler.web.repositories;

import com.mycrawler.web.entities.StockEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface StockRepository extends ListCrudRepository<StockEntity, Long> {
    StockEntity findByUrl(String url);
}
