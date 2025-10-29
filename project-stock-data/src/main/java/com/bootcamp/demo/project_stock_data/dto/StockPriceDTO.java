package com.bootcamp.demo.project_stock_data.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
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

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Hong_Kong")
  @Builder.Default
  private LocalDateTime datetime = LocalDateTime.now();
}