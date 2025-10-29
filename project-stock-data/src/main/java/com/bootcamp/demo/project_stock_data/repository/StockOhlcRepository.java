package com.bootcamp.demo.project_stock_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bootcamp.demo.project_stock_data.entity.StockOhlcEntity;
import java.util.List;

public interface StockOhlcRepository extends JpaRepository<StockOhlcEntity, Long> {
    List<StockOhlcEntity> findBySymbolOrderByDateDesc(String symbol);
}