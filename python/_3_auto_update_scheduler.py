import schedule
import time
from _2_load_ohlcv_data import main

# 每日凌晨 03:00 自動更新最近 5 日資料
schedule.every().day.at("03:00").do(main, full_mode=False)

print("🕓 Scheduler started... (每晚 3:00 自動補數)")

while True:
    schedule.run_pending()
    time.sleep(60)