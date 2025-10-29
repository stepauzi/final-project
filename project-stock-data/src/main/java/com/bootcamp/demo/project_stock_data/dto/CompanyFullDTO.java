package com.bootcamp.demo.project_stock_data.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyFullDTO {
    private FinnhubCompanyDTO companyInfo;
    private StockProfileDTO stockPrice;
}
