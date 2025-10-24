package com.bootcamp.demo.project_data_provider.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.bootcamp.demo.project_data_provider.dto.CompanyFullDTO;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.FinnhubCompanyDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockApiController {

    private final StockApiService stockApiService;

    // 🔹 Top N 公司 + 即時股價
    @GetMapping("/top")
    public List<CompanyFullDTO> getTopCompanies(@RequestParam(defaultValue = "10") int limit) {
        return stockApiService.getTopCompanies(limit);
    }

    // 🔹 單一公司即時股價
    @GetMapping("/{symbol}")
    public StockPriceDTO getStockPrice(@PathVariable String symbol) {
        return stockApiService.getStockPrice(symbol);
    }

    // 🔹 單一公司基本資料
    @GetMapping("/company/{symbol}")
    public FinnhubCompanyDTO getCompanyInfo(@PathVariable String symbol) {
        return stockApiService.getCompanyInfo(symbol);
    }

    // 🔹 單一公司全部資訊（公司資料 + 即時股價）
    @GetMapping("/full/{symbol}")
    public CompanyFullDTO getFullCompany(@PathVariable String symbol) {
        return stockApiService.getFullCompany(symbol);
    }
}
