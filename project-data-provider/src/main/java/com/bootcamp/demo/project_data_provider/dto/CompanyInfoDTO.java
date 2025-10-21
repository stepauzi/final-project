package com.bootcamp.demo.project_data_provider.dto;

import lombok.Data;

@Data
public class CompanyInfoDTO {
    private String symbol;
    private String name;
    private String industry;
    private String logo;
    private Long sharesOutstanding;
    private Long marketCap;

    // 即時股價相關
    private Double currentPrice;
    private Double prevClose;
    private Double change;
    private Double changePercent;
    private Long volume;
}


