package com.bootcamp.demo.project_stock_data.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.bootcamp.demo.project_stock_data.service.StockDataService;
import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final StockDataService stockService;
    private final StartupConfig startupConfig; // 🟢 注入 StartupConfig 方便調用

    // 🔁 每日 5AM：更新 symbol list (Python script)
    @Scheduled(cron = "0 0 5 * * ?")
    public void dailyLoadSymbols() {
        startupConfig.runPythonScript();
        System.out.println("✅ Daily stock symbols reloaded from CSV");
    }

    // 🔁 每日 6AM：更新 stock profiles (API)
    @Scheduled(cron = "0 0 6 * * ?")
    public void autoRefreshProfiles() {
        stockService.refreshAllStockProfiles();
        System.out.println("✅ Daily stock data refreshed");
    }
}