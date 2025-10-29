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

    @GetMapping("/refresh-all")
public List<StockProfileEntity> refreshAll() {
    return stockDataService.refreshAllStockProfiles();
}

@GetMapping("/all")
public List<StockProfileEntity> getAll() {
    return stockDataService.getAllProfiles();
}
}
