package com.bootcamp.demo.project_data_provider.dto;

import com.bootcamp.demo.project_data_provider.finnhub.model.dto.FinnhubCompanyDTO;
import lombok.Builder;
import lombok.Getter;

// Inerternal DTO
@Getter
@Builder
public class CompanyFullDTO {
    private StockPriceDTO stockPrice;
    private FinnhubCompanyDTO companyInfo;
}
