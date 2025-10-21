package com.bootcamp.demo.project_data_provider.service;

import com.bootcamp.demo.project_data_provider.dto.CompanyInfoDTO;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;

import java.util.List;

public interface StockApiService {

    CompanyInfoDTO getCompanyInfo(String symbol);

    StockPriceDTO getStockPrice(String symbol);

    List<CompanyInfoDTO> getTopCompanies(int limit);
}
