package com.bootcamp.demo.project_data_provider.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class StockPriceDTO {

    private String symbol;

    @JsonAlias("c") // current price
    private Double currentPrice;

    @JsonAlias("pc") // previous close
    private Double prevClose;

    @JsonAlias("v") // volume
    private Long volume;

    // 自動計算價差
    public Double getChange() {
        if (currentPrice != null && prevClose != null)
            return currentPrice - prevClose;
        return null;
    }

    // 自動計算漲跌幅%
    public Double getChangePercent() {
        if (currentPrice != null && prevClose != null && prevClose != 0)
            return ((currentPrice - prevClose) / prevClose) * 100;
        return null;
    }
}
