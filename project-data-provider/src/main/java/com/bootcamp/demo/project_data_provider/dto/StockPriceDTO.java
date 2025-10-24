package com.bootcamp.demo.project_data_provider.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

// Inerternal DTO
@Getter
@Builder
public class StockPriceDTO {
  private String symbol;
  private Double price;
  private Double dayHigh;
  private Double dayLow;
  private Double dayOpen;

  @Builder.Default
  private LocalDateTime datetime = LocalDateTime.now();
}