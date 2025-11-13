package com.mycrawler.web.jobs.importdata.stores;

import com.mycrawler.web.entities.StoreEntity;
import com.mycrawler.web.repositories.StoreRepository;
import org.springframework.batch.item.ItemProcessor;

public class ImportStoresProcessor implements ItemProcessor<StoresData, StoreEntity> {

    private final StoreRepository storeRepository;

    public ImportStoresProcessor(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public StoreEntity process(StoresData item) throws Exception {
        StoreEntity existingStore = storeRepository.findByStoreCode(item.code());
        if(existingStore != null) return null;

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreName(item.name());
        storeEntity.setStoreCode(item.code());
        return storeEntity;
    }
}
