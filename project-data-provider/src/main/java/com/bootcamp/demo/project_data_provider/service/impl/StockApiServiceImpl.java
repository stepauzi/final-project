package com.bootcamp.demo.project_data_provider.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.demo.project_data_provider.dto.CompanyFullDTO;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.FinnhubCompanyDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.FinnhubStockDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockApiServiceImpl implements StockApiService {

    private final RestTemplate restTemplate;

    @Value("${api-service.finnhub.api-token}")
    private String apiKey;

    // 🔹 Finnhub API Endpoints
    private static final String FINNHUB_BASE_URL = "https://finnhub.io/api/v1";
    private static final String QUOTE_ENDPOINT = "/quote";
    private static final String PROFILE_ENDPOINT = "/stock/profile2";

    // ✅ 即時股價（Stock Price）
    @Override
    public StockPriceDTO getStockPrice(String symbol) {
        String url = String.format("%s%s?symbol=%s&token=%s", 
                                   FINNHUB_BASE_URL, QUOTE_ENDPOINT, symbol, apiKey);
        log.info("📡 Fetching stock price for {}", symbol);

        FinnhubStockDTO quote = restTemplate.getForObject(url, FinnhubStockDTO.class);

        if (quote == null) {
            log.warn("⚠️ No data returned for symbol {}", symbol);
            return null;
        }

        return StockPriceDTO.builder()
                .symbol(symbol)
                .price(quote.getCurrentPrice())
                .dayHigh(quote.getHigh())
                .dayLow(quote.getLow())
                .dayOpen(quote.getOpen())
                .build();
    }

    // ✅ 公司資料（Company Info）
    @Override
    public FinnhubCompanyDTO getCompanyInfo(String symbol) {
        String url = String.format("%s%s?symbol=%s&token=%s", 
                                   FINNHUB_BASE_URL, PROFILE_ENDPOINT, symbol, apiKey);
        log.info("🏢 Fetching company info for {}", symbol);

        FinnhubCompanyDTO company = restTemplate.getForObject(url, FinnhubCompanyDTO.class);

        if (company == null) {
            log.warn("⚠️ No company info found for symbol {}", symbol);
            return null;
        }

        return company;
    }

    // ✅ 公司 + 即時股價（Full Data）
    @Override
    public CompanyFullDTO getFullCompany(String symbol) {
        FinnhubCompanyDTO info = getCompanyInfo(symbol);
        StockPriceDTO price = getStockPrice(symbol);

        // ⚙️ 修正 null symbol 問題（保證 Company 有 symbol）
        if (info != null && (info.getSymbol() == null || info.getSymbol().isEmpty())) {
            info.setSymbol(symbol);
        }

        return CompanyFullDTO.builder()
                .companyInfo(info)
                .stockPrice(price)
                .build();
    }
}
