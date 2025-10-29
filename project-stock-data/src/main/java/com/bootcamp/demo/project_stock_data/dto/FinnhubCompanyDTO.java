package com.bootcamp.demo.project_stock_data.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

// External DTO
@Getter 
@Setter
public class FinnhubCompanyDTO {
    private String symbol;

    @JsonAlias("name")
    private String name;

    @JsonAlias("finnhubIndustry")
    private String industry;

    @JsonAlias("logo")
    private String logo;

    @JsonAlias("shareOutstanding")
    private Long sharesOutstanding;

    @JsonAlias("marketCapitalization")
    private Long marketCap;
}

