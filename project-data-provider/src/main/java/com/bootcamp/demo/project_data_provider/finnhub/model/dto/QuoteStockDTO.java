package com.bootcamp.demo.project_data_provider.finnhub.model.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


// Exteral DTO
@Getter
@Setter
public class QuoteStockDTO {
  @JsonProperty("c")  // current price
  private Double currentPrice;

  @JsonProperty("h")  // high price of the day
  private Double high;

  @JsonProperty("l")  // low price of the day
  private Double low;

  @JsonProperty("o")  // open price
  private Double open;

  @JsonProperty("pc") // previous close
  private Double prevClose;
}