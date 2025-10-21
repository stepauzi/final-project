package com.bootcamp.demo.project_data_provider.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CompanyInfoDTO {
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

