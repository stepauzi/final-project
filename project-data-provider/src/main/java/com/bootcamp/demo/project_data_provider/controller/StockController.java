package com.bootcamp.demo.project_data_provider.controller;

import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockApiService stockApiService;

    @GetMapping("/{symbol}")
    public StockPriceDTO getStock(@PathVariable String symbol) {
        return stockApiService.getRealTimePrice(symbol);
    }
}
