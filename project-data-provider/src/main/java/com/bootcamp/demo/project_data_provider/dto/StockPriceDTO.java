package com.bootcamp.demo.project_data_provider.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data // Internal DTO
public class StockPriceDTO {

    private String symbol;

    @JsonAlias("c") // current price
    private Double currentPrice;

    @JsonAlias("pc") // previous close
    private Double prevClose;

    public Double getChange() {
        if (currentPrice != null && prevClose != null)
            return currentPrice - prevClose;
        return null;
    }

    public Double getChangePercent() {
        if (currentPrice != null && prevClose != null && prevClose != 0)
            return ((currentPrice - prevClose) / prevClose) * 100;
        return null;
    }
}
