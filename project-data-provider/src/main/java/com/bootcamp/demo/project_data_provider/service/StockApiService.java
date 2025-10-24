package com.bootcamp.demo.project_data_provider.service;

import java.util.List;
import com.bootcamp.demo.project_data_provider.dto.CompanyFullDTO;
import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.finnhub.model.dto.QuoteCompanyDTO;

public interface StockApiService {

    StockPriceDTO getStockPrice(String symbol);

    QuoteCompanyDTO getCompanyInfo(String symbol);

    CompanyFullDTO getFullCompany(String symbol);

    List<CompanyFullDTO> getTopCompanies(int limit);
}
