package com.bootcamp.demo.project_stock_data.service;

import com.bootcamp.demo.project_stock_data.entity.StockOhlcEntity;
import java.util.List;

public interface StockOhlcService {
    List<StockOhlcEntity> getHistoryBySymbol(String symbol);
}