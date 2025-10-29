package com.bootcamp.demo.project_stock_data.controller;

import com.bootcamp.demo.project_stock_data.entity.StockProfileEntity;
import com.bootcamp.demo.project_stock_data.service.StockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-api")
@RequiredArgsConstructor
public class StockDataController {

private final StockDataService stockDataService;

// call API
@GetMapping("/refresh-all")
public List<StockProfileEntity> refreshAll() {
    return stockDataService.refreshAllStockProfiles();
}

// from DB
@GetMapping("/all")
public List<StockProfileEntity> getAll() {
    return stockDataService.getAllProfiles();
}

@PostMapping("/refresh-missing")
public List<StockProfileEntity> refreshMissing() {
    return stockDataService.refreshMissingStocks();
}
}
