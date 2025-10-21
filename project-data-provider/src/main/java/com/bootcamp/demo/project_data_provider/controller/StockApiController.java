package com.bootcamp.demo.project_data_provider.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.dto.CompanyInfoDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockApiController {

    private final StockApiService stockApiService;

    @GetMapping("/top")
    public List<CompanyInfoDTO> getTopCompanies(@RequestParam(defaultValue = "10") int limit) {
        return stockApiService.getTopCompanies(limit);
    }

    @GetMapping("/{symbol}")
    public StockPriceDTO getStockPrice(@PathVariable String symbol) {
        return stockApiService.getStockPrice(symbol);
    }

    @GetMapping("/company/{symbol}")
    public CompanyInfoDTO getCompanyInfo(@PathVariable String symbol) {
        return stockApiService.getCompanyInfo(symbol);
    }
}
