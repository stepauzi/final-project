package com.bootcamp.demo.project_stock_data.service;

import java.util.List;
import com.bootcamp.demo.project_stock_data.entity.StockProfileEntity;

public interface StockDataService {
    List<StockProfileEntity> refreshAllStockProfiles();
    List<StockProfileEntity> getAllProfiles();
    List<StockProfileEntity> refreshMissingStocks();
}