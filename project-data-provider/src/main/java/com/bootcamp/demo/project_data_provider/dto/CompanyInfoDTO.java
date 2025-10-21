package com.bootcamp.demo.project_data_provider.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CompanyInfoDTO {

    @JsonAlias("ticker")
    private String symbol;

    @JsonAlias("name")
    private String companyName;

    @JsonAlias("finnhubIndustry")
    private String industry;

    @JsonAlias("marketCapitalization")
    private Double marketCap;

    @JsonAlias("shareOutstanding")
    private Double shares;

    @JsonAlias("logo")
    private String logoUrl;
}
