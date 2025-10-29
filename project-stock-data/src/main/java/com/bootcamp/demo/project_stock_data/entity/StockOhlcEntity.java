package com.bootcamp.demo.project_stock_data.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_ohlc")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StockOhlcEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private LocalDate date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;

    @ManyToOne
    @JoinColumn(name = "symbol", referencedColumnName = "symbol", insertable = false, updatable = false)
    private StockProfileEntity profile;
}