package com.bootcamp.demo.project_stock_data.service.impl;

import com.bootcamp.demo.project_stock_data.dto.CompanyFullDTO;
import com.bootcamp.demo.project_stock_data.entity.StockProfileEntity;
import com.bootcamp.demo.project_stock_data.entity.StockSymbolEntity;
import com.bootcamp.demo.project_stock_data.repository.StockProfileRepository;
import com.bootcamp.demo.project_stock_data.repository.StockSymbolRepository;
import com.bootcamp.demo.project_stock_data.service.StockDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockDataServiceImpl implements StockDataService {

    private final RestTemplate restTemplate;
    private final StockProfileRepository stockProfileRepository;
    private final StockSymbolRepository stockSymbolRepository;

    private static final String DATA_PROVIDER_URL = "http://localhost:8081/api/full/";

    @Override
    public List<StockProfileEntity> refreshAllStockProfiles() {
        List<StockSymbolEntity> symbols = stockSymbolRepository.findAll();
        List<StockProfileEntity> savedProfiles = new ArrayList<>();

        log.info("🚀 Refreshing stock data for {} symbols", symbols.size());

        for (StockSymbolEntity s : symbols) {
            String symbol = s.getSymbol();

            try {
                String url = DATA_PROVIDER_URL + symbol;
                CompanyFullDTO dto = restTemplate.getForObject(url, CompanyFullDTO.class);

                if (dto == null || dto.getCompanyInfo() == null || dto.getStockPrice() == null) {
                    log.warn("⚠️ Skipped {}, no data returned", symbol);
                    continue;
                }

                StockProfileEntity entity = StockProfileEntity.builder()
                        .symbol(symbol)
                        .name(dto.getCompanyInfo().getName())
                        .industry(dto.getCompanyInfo().getIndustry())
                        .logo(dto.getCompanyInfo().getLogo())
                        .sharesOutstanding(dto.getCompanyInfo().getSharesOutstanding())
                        .marketCap(dto.getCompanyInfo().getMarketCap())
                        .latestPrice(dto.getStockPrice().getPrice())
                        .dayHigh(dto.getStockPrice().getDayHigh())
                        .dayLow(dto.getStockPrice().getDayLow())
                        .dayOpen(dto.getStockPrice().getDayOpen())
                        .lastUpdated(dto.getStockPrice().getDatetime())
                        .build();

                stockProfileRepository.save(entity);
                savedProfiles.add(entity);

                Thread.sleep(3000); // 🕒 避免 overload Finnhub API
            } catch (Exception e) {
                log.error("❌ Failed to fetch data for {}: {}", symbol, e.getMessage());
            }
        }

        log.info("✅ Successfully refreshed {} stock profiles", savedProfiles.size());
        return savedProfiles;
    }

    @Override
    public List<StockProfileEntity> getAllProfiles() {
        return stockProfileRepository.findAll();
    }
}
