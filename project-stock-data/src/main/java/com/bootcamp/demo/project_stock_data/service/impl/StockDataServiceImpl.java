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

    // ✅ 主更新邏輯：Refresh 全部股票資料
    @Override
    public List<StockProfileEntity> refreshAllStockProfiles() {
        List<StockSymbolEntity> symbols = stockSymbolRepository.findAll();
        List<StockProfileEntity> savedProfiles = new ArrayList<>();

        for (StockSymbolEntity s : symbols) {
            try {
                String url = DATA_PROVIDER_URL + s.getSymbol();
                CompanyFullDTO dto = restTemplate.getForObject(url, CompanyFullDTO.class);

                if (dto == null || dto.getCompanyInfo() == null || dto.getStockPrice() == null)
                    continue;

                StockProfileEntity entity = StockProfileEntity.builder()
                        .symbol(dto.getStockPrice().getSymbol())
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
                Thread.sleep(3000); // ⏳ API 限流保護

            } catch (Exception e) {
                log.warn("❌ Failed to refresh data for {}: {}", s.getSymbol(), e.getMessage());
            }
        }

        log.info("✅ Successfully refreshed {} stock profiles", savedProfiles.size());

        // 🚀 自動補漏
        List<StockProfileEntity> missing = refreshMissingStocks();
        log.info("🧩 Auto-filled {} missing profiles", missing.size());

        return savedProfiles;
    }

    // ✅ 查詢所有已存在資料
    @Override
    public List<StockProfileEntity> getAllProfiles() {
        return stockProfileRepository.findAll();
    }

    // ✅ 自動檢查並補漏
    @Override
    public List<StockProfileEntity> refreshMissingStocks() {
        // 從 DB 取出全部 symbol
        List<String> allSymbols = stockSymbolRepository.findAll()
                .stream()
                .map(StockSymbolEntity::getSymbol)
                .toList();

        // 已存在於 profiles 嘅 symbol
        List<String> existingSymbols = stockProfileRepository.findAll()
                .stream()
                .map(StockProfileEntity::getSymbol)
                .toList();

        // 計出未有的 symbol
        List<String> missingSymbols = allSymbols.stream()
                .filter(sym -> !existingSymbols.contains(sym))
                .toList();

        if (missingSymbols.isEmpty()) {
            log.info("🎯 No missing stocks found. All up to date!");
            return Collections.emptyList();
        }

        log.info("🔍 Found {} missing symbols: {}", missingSymbols.size(), missingSymbols);

        List<StockProfileEntity> updated = new ArrayList<>();

        for (String symbol : missingSymbols) {
            try {
                String url = DATA_PROVIDER_URL + symbol;
                CompanyFullDTO dto = restTemplate.getForObject(url, CompanyFullDTO.class);

                if (dto == null || dto.getCompanyInfo() == null || dto.getStockPrice() == null) {
                    log.warn("⚠️ No data for {}", symbol);
                    continue;
                }

                StockProfileEntity entity = StockProfileEntity.builder()
                        .symbol(dto.getStockPrice().getSymbol())
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
                updated.add(entity);
                Thread.sleep(3000);

            } catch (Exception e) {
                log.warn("❌ Failed to refresh {}: {}", symbol, e.getMessage());
            }
        }

        return updated;
    }
}
