package com.bootcamp.demo.project_stock_data.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class StartupConfig {

    // ✅ 共用 method（供 Scheduler 重用）
    public void runPythonScript() {
        try {
            System.out.println("🚀 Checking and loading stock symbols from Python...");

            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File pythonScript = new File(projectRoot, "python/_1_load_snp500_symbol.py");

            if (!pythonScript.exists()) {
                System.err.println("❌ Python script not found at: " + pythonScript.getAbsolutePath());
                return;
            }

            ProcessBuilder pb = new ProcessBuilder("python3", pythonScript.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[PYTHON] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Python script finished successfully.");
            } else {
                System.out.println("⚠️ Python script exited with code " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to run Python script: " + e.getMessage());
        }
    }

    // 🟢 系統啟動時自動執行
    @PostConstruct
    public void onStartup() {
        runPythonScript();
    }
}