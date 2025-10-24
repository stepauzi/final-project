package com.bootcamp.demo.project_data_provider.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.demo.project_data_provider.dto.CompanyFullDTO;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.QuoteCompanyDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.QuoteStockDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockApiServiceImpl implements StockApiService {

    private final RestTemplate restTemplate;

    @Value("${api-service.finnhub.api-token}")
    private String apiKey;

    // ✅ 單一公司即時股價
    @Override
    public StockPriceDTO getStockPrice(String symbol) {
        String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;

        // 用 External DTO 來接 API
        QuoteStockDTO quote = restTemplate.getForObject(url, QuoteStockDTO.class);
        if (quote == null) return null;

        // 將 External DTO 轉成 Internal DTO
        return StockPriceDTO.builder()
                .symbol(symbol)
                .price(quote.getCurrentPrice())
                .dayHigh(quote.getHigh())
                .dayLow(quote.getLow())
                .dayOpen(quote.getOpen())
                .build();
    }

    // ✅ 單一公司資料
    @Override
    public QuoteCompanyDTO getCompanyInfo(String symbol) {
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + apiKey;

        QuoteCompanyDTO company = restTemplate.getForObject(url, QuoteCompanyDTO.class);
        if (company != null) {
            // 設定 symbol（Finnhub 有時無返）
            try {
                var field = company.getClass().getDeclaredField("symbol");
                field.setAccessible(true);
                field.set(company, symbol);
            } catch (Exception e) {
                // ignore
            }
        }
        return company;
    }

    // ✅ 單一公司全部資訊（資料 + 股價）
    @Override
    public CompanyFullDTO getFullCompany(String symbol) {
        QuoteCompanyDTO info = getCompanyInfo(symbol);
        StockPriceDTO price = getStockPrice(symbol);

        // 用 Constructor 或 Setter 組合
        CompanyFullDTO full = new CompanyFullDTO();
        full.setCompanyInfo(info);
        full.setStockPrice(price);
        return full;
    }

    // ✅ Top N 公司（組合）
    @Override
    public List<CompanyFullDTO> getTopCompanies(int limit) {
        List<String> symbols = Arrays.asList(
            "AAPL","MSFT","GOOGL","AMZN","TSLA",
            "NVDA","META","BRK.A","V","JPM",
            "JNJ","XOM","PG","UNH","HD"
        );

        List<CompanyFullDTO> topList = new ArrayList<>();
        for (String sym : symbols) {
            CompanyFullDTO full = getFullCompany(sym);
            if (full != null) topList.add(full);
        }
        return topList.subList(0, Math.min(limit, topList.size()));
    }
}