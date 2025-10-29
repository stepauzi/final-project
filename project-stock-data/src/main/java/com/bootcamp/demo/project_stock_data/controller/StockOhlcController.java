package com.bootcamp.demo.project_stock_data.controller;

import com.bootcamp.demo.project_stock_data.entity.StockOhlcEntity;
import com.bootcamp.demo.project_stock_data.repository.StockOhlcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stock-api/ohlc")
@RequiredArgsConstructor
public class StockOhlcController {

    private final StockOhlcRepository stockOhlcRepository;

    @GetMapping("/{symbol}")
    public List<StockOhlcEntity> getOhlcBySymbol(@PathVariable String symbol) {
        return stockOhlcRepository.findBySymbolOrderByDateDesc(symbol);
    }

        // 🔹 查全部 (for testing only)
    @GetMapping("/all")
    public List<StockOhlcEntity> getAllOhlc() {
        return stockOhlcRepository.findAll();
    }
}