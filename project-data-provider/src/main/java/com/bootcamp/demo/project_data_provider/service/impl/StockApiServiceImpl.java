package com.bootcamp.demo.project_data_provider.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.dto.CompanyInfoDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;

@Service
public class StockApiServiceImpl implements StockApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "d3rfn5pr01qopgh8bod0d3rfn5pr01qopgh8bodg";

    @Override
    public StockPriceDTO getStockPrice(String symbol) {
        String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
        StockPriceDTO dto = restTemplate.getForObject(url, StockPriceDTO.class);
        if (dto != null) dto.setSymbol(symbol);
        return dto;
    }

    @Override
    public CompanyInfoDTO getCompanyInfo(String symbol) {
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + apiKey;
        CompanyInfoDTO dto = restTemplate.getForObject(url, CompanyInfoDTO.class);
        if (dto != null) dto.setSymbol(symbol);
        return dto;
    }

    @Override
    public List<CompanyInfoDTO> getTopCompanies(int limit) {
        List<String> symbols = Arrays.asList(
            "AAPL","MSFT","GOOGL","AMZN","TSLA",
            "NVDA","META","BRK.A","V","JPM",
            "JNJ","XOM","PG","UNH","HD" // ! need replace top 15 of the industry
        );

        List<CompanyInfoDTO> companies = new ArrayList<>();

        for (String sym : symbols) {
            CompanyInfoDTO info = getCompanyInfo(sym);
            StockPriceDTO price = getStockPrice(sym);
            if (info != null && price != null) {
                info.setCurrentPrice(price.getCurrentPrice());
                info.setPrevClose(price.getPrevClose());
                info.setChange(price.getChange());
                info.setChangePercent(price.getChangePercent());
                info.setVolume(price.getVolume());
                companies.add(info);
            }
        }

        return companies.subList(0, Math.min(limit, companies.size()));
    }
}
