package com.bootcamp.demo.project_stock_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bootcamp.demo.project_stock_data.entity.StockSymbolEntity;

@Repository
public interface StockSymbolRepository extends JpaRepository<StockSymbolEntity, String> {

}
