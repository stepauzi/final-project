package com.bootcamp.demo.project_data_provider.finnhub.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data // Exteral DTO
public class QuoteStockDTO {
    @JsonAlias("c")  private Double currentPrice;
    @JsonAlias("h")  private Double high;
    @JsonAlias("l")  private Double low;
    @JsonAlias("o")  private Double open;
    @JsonAlias("pc") private Double previousClose;
}
