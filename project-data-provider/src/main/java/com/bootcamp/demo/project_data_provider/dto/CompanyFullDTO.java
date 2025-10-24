package com.bootcamp.demo.project_data_provider.dto;

import com.bootcamp.demo.project_data_provider.finnhub.model.dto.QuoteCompanyDTO;
import lombok.Getter;
import lombok.Setter;

// Inerternal DTO
@Getter
@Setter
public class CompanyFullDTO {
    private QuoteCompanyDTO companyInfo;
    private StockPriceDTO stockPrice;
}
