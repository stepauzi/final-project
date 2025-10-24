package com.bootcamp.demo.project_data_provider.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.demo.project_data_provider.dto.CompanyFullDTO;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.QuoteCompanyDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;

@Service
public class StockApiServiceImpl implements StockApiService {

    private final RestTemplate restTemplate;

    @Value("${api-service.finnhub.api-token}")
    private String apiKey;

    public StockApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public StockPriceDTO getStockPrice(String symbol) {
        String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
        StockPriceDTO dto = restTemplate.getForObject(url, StockPriceDTO.class);
        if (dto != null) dto.setSymbol(symbol);
        return dto;
    }

    @Override
    public QuoteCompanyDTO getCompanyInfo(String symbol) {
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + apiKey;
        QuoteCompanyDTO dto = restTemplate.getForObject(url, QuoteCompanyDTO.class);
        if (dto != null) dto.setSymbol(symbol);
        return dto;
    }

    @Override
    public CompanyFullDTO getFullCompany(String symbol) {
        QuoteCompanyDTO info = getCompanyInfo(symbol);
        StockPriceDTO price = getStockPrice(symbol);

        CompanyFullDTO full = new CompanyFullDTO();
        full.setCompanyInfo(info);
        full.setStockPrice(price);
        return full;
    }

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