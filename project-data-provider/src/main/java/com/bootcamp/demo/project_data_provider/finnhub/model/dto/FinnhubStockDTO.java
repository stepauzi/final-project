package com.bootcamp.demo.project_data_provider.finnhub.model.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// External DTO
@Getter
@Setter
public class FinnhubStockDTO {
  @JsonProperty("c")
  private Double currentPrice;

  @JsonProperty("h")
  private Double high;

  @JsonProperty("l")
  private Double low;

  @JsonProperty("o") 
  private Double open;

  @JsonProperty("pc")
  private Double prevClose;
}