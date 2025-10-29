package com.bootcamp.demo.project_stock_data.service.impl;

import com.bootcamp.demo.project_stock_data.entity.StockOhlcEntity;
import com.bootcamp.demo.project_stock_data.repository.StockOhlcRepository;
import com.bootcamp.demo.project_stock_data.service.StockOhlcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockOhlcServiceImpl implements StockOhlcService {

    private final StockOhlcRepository repo;

    @Override
    public List<StockOhlcEntity> getHistoryBySymbol(String symbol) {
        return repo.findBySymbolOrderByDateDesc(symbol);
    }
}