package com.mycrawler.web.repositories;

import com.mycrawler.web.entities.StoreEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface StoreRepository extends ListCrudRepository<StoreEntity, Long> {
    StoreEntity findByStoreCode(String storeCode);
}
