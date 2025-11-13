package com.mycrawler.web.converters;


import com.mycrawler.web.entities.StockEntity;
import com.mycrawler.web.views.StockView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StockConverter implements Converter<StockEntity, StockView> {

    @Override
    public StockView convert(StockEntity stockEntity) {
        return new StockView(
                stockEntity.getStore().getStoreCode(),
                stockEntity.getStore().getStoreName(),
                stockEntity.getUrl(),
                stockEntity.getPrice());
    }
}
