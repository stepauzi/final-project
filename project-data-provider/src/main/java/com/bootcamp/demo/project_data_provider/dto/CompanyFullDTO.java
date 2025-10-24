package com.bootcamp.demo.project_data_provider.dto;

import com.bootcamp.demo.project_data_provider.finnhub.model.dto.QuoteCompanyDTO;
import lombok.Data;

@Data // Inerternal DTO
public class CompanyFullDTO {
    private QuoteCompanyDTO companyInfo;
    private StockPriceDTO stockPrice;
}
