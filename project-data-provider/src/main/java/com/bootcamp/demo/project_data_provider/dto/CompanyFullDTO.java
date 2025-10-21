package com.bootcamp.demo.project_data_provider.dto;

import lombok.Data;

@Data
public class CompanyFullDTO {
    private CompanyInfoDTO companyInfo;
    private StockPriceDTO stockPrice;
}
