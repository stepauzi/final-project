package com.bootcamp.demo.project_stock_data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockProfileEntity {

    @Id
    private String symbol;

    private String name;
    private String industry;
    private String logo;

    private Long sharesOutstanding;
    private Long marketCap;

    private Double latestPrice;
    private Double dayHigh;
    private Double dayLow;
    private Double dayOpen;

    private String lastUpdated;

@JsonIgnore
@ManyToOne
@JoinColumn(name = "symbol", referencedColumnName = "symbol", insertable = false, updatable = false)
private StockSymbolEntity stockSymbol; 
}