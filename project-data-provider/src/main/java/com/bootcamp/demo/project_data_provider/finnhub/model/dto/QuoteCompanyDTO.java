package com.bootcamp.demo.project_data_provider.finnhub.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

// External DTO
@Getter 
public class QuoteCompanyDTO {
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

