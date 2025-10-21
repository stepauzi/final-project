package com.bootcamp.demo.project_data_provider.service;

import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;

public interface StockApiService {
    StockPriceDTO getRealTimePrice(String symbol);
}
