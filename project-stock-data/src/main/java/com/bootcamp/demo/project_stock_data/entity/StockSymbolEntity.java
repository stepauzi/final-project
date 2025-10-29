package com.bootcamp.demo.project_stock_data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_symbols")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockSymbolEntity {

    // 🔑 主鍵：symbol
    @Id
    @Column(length = 10, nullable = false, unique = true)
    private String symbol;

    // 公司名稱
    @Column(length = 255)
    private String name;

    // 行業分類
    @Column(length = 255)
    private String industry;
}