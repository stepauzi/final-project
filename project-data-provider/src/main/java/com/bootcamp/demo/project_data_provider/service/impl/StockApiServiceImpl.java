package com.bootcamp.demo.project_data_provider.service.impl;

import com.bootcamp.demo.project_data_provider.dto.StockPriceDTO;
import com.bootcamp.demo.project_data_provider.service.StockApiService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StockApiServiceImpl implements StockApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public StockPriceDTO getRealTimePrice(String symbol) {

        String apiKey = "d3rfn5pr01qopgh8bod0d3rfn5pr01qopgh8bodg"; 
        String url = UriComponentsBuilder
                .fromHttpUrl("https://finnhub.io/api/v1/quote")
                .queryParam("symbol", symbol)
                .queryParam("token", apiKey)
                .toUriString();

        StockPriceDTO dto = restTemplate.getForObject(url, StockPriceDTO.class);
        if (dto != null) dto.setSymbol(symbol);
        return dto;
    }
}
