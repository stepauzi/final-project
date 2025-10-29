package com.bootcamp.demo.project_stock_data.mapper;

import com.bootcamp.demo.project_stock_data.dto.StockProfileDTO;
import com.bootcamp.demo.project_stock_data.entity.StockProfileEntity;
import java.util.UUID; // ✅ 必須 import 呢個

public class StockMapper {

    public static StockProfileEntity toEntity(StockProfileDTO dto) {
        if (dto == null) return null;

        String symbol = dto.getSymbol();
        if (symbol == null || symbol.isEmpty()) {
            System.out.println("⚠️ Missing symbol in DTO: " + dto.getName());
            symbol = "UNKNOWN_" + UUID.randomUUID().toString().substring(0, 8);
        }

        return StockProfileEntity.builder()
                .symbol(symbol)
                .name(dto.getName())
                .industry(dto.getIndustry())
                .logo(dto.getLogo())
                .sharesOutstanding(dto.getSharesOutstanding())
                .marketCap(dto.getMarketCap())
                .latestPrice(dto.getPrice())
                .dayHigh(dto.getDayHigh())
                .dayLow(dto.getDayLow())
                .dayOpen(dto.getDayOpen())
                .lastUpdated(dto.getDatetime())
                .build();
    }
}