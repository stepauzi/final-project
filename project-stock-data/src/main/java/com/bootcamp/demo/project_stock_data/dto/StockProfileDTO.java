package com.bootcamp.demo.project_stock_data.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StockProfileDTO {
    private String symbol;
    private String name;
    private String industry;
    private String logo;
    private Long sharesOutstanding;
    private Long marketCap;

    private Double price;
    private Double dayHigh;
    private Double dayLow;
    private Double dayOpen;
    private String datetime;
}