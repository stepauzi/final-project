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

    // 🔹 Constants for endpoints
    private static final String FINNHUB_BASE_URL = "https://finnhub.io/api/v1";
    private static final String QUOTE_ENDPOINT = "/quote";
    private static final String PROFILE_ENDPOINT = "/stock/profile2";

    // ✅ 即時股價
    @Override
    public StockPriceDTO getStockPrice(String symbol) {
        String url = String.format("%s%s?symbol=%s&token=%s", FINNHUB_BASE_URL, QUOTE_ENDPOINT, symbol, apiKey);
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

    // ✅ 公司資料
    @Override
    public FinnhubCompanyDTO getCompanyInfo(String symbol) {
        String url = String.format("%s%s?symbol=%s&token=%s", FINNHUB_BASE_URL, PROFILE_ENDPOINT, symbol, apiKey);
        log.info("🏢 Fetching company info for {}", symbol);

        FinnhubCompanyDTO company = restTemplate.getForObject(url, FinnhubCompanyDTO.class);
        if (company == null) {
            log.warn("⚠️ No company info found for symbol {}", symbol);
            return null;
        }
        return company;
    }

    // ✅ 全部公司資訊（公司 + 即時價）
    @Override
    public CompanyFullDTO getFullCompany(String symbol) {
        FinnhubCompanyDTO info = getCompanyInfo(symbol);
        StockPriceDTO price = getStockPrice(symbol);

    // ⚙️ 修正 null symbol 問題
    if (info != null && (info.getSymbol() == null || info.getSymbol().isEmpty())) {
        info.setSymbol(symbol); // 或者用 price.getSymbol() 亦可
    }
        return CompanyFullDTO.builder()
                .companyInfo(info)
                .stockPrice(price)
                .build();
    }

    // ✅ Top N 公司（組合）
    @Override
    public List<CompanyFullDTO> getTopCompanies(int limit) {
        List<String> symbols = Arrays.asList(
            "AAPL","MSFT","GOOGL","AMZN","TSLA",
            "NVDA","META","BRK.A","V","JPM",
            "JNJ","XOM","PG","UNH","HD"
        );

        log.info("🚀 Fetching top {} companies", limit);

        List<CompanyFullDTO> topList = new ArrayList<>();
        for (String sym : symbols) {
            CompanyFullDTO full = getFullCompany(sym);
            if (full != null) topList.add(full);
        }

        return topList.subList(0, Math.min(limit, topList.size()));
    }

@Override
public List<CompanyFullDTO> getAllStocks() {
    String csvUrl = "https://raw.githubusercontent.com/datasets/s-and-p-500-companies/master/data/constituents.csv";
    List<CompanyFullDTO> result = new ArrayList<>();

    try {
        // 🧩 讀取 CSV（用 pandas 類似效果）
        String content = new String(new java.net.URL(csvUrl).openStream().readAllBytes());
        String[] lines = content.split("\n");

        for (int i = 1; i < lines.length; i++) { // skip header
            String[] parts = lines[i].split(",");
            if (parts.length > 0) {
                String symbol = parts[0].replace("\"", "").trim();

                // 🕵️ Normalize symbol (BRK-B → BRK.A)
                if (symbol.equals("BRK-B")) symbol = "BRK.A";
                if (symbol.equals("BF-B")) symbol = "BF.B";

                CompanyFullDTO dto = getFullCompany(symbol);
                if (dto != null) {
                    result.add(dto);
                    Thread.sleep(1000); // 防止 API 限流
                }
            }
        }

    } catch (Exception e) {
        log.error("❌ Error loading symbols: {}", e.getMessage());
    }

    log.info("✅ Loaded {} companies from CSV", result.size());
    return result;
}

}