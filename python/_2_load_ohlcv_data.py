import pandas as pd
import yfinance as yf
import time, random, os
from datetime import datetime
from sqlalchemy import create_engine, text

# ---------------------------------------
# ⚙️ PostgreSQL 連線設定
# ---------------------------------------
DB_URL = "postgresql+psycopg2://postgres:1234@localhost/stockdb"
engine = create_engine(DB_URL, pool_pre_ping=True)

MAX_RETRY = 3
FAILED_FILE = "failed_symbols.log"

# ---------------------------------------
# 🔧 Symbol 修正 (BRK.A → BRK-A)
# ---------------------------------------
def normalize_symbol(symbol: str) -> str:
    """轉成 Yahoo Finance 可接受格式"""
    if "." in symbol and not symbol.endswith((".HK", ".L", ".TO")):
        return symbol.replace(".", "-")
    return symbol

# ---------------------------------------
# 📈 抓取單隻股票 (含 retry)
# ---------------------------------------
def fetch_yahoo_ohlc(symbol: str, period="5d"):
    for attempt in range(1, MAX_RETRY + 1):
        fixed_symbol = normalize_symbol(symbol)
        print(f"📊 Fetching {symbol} (Yahoo: {fixed_symbol}) [try {attempt}/{MAX_RETRY}] ...")
        try:
            data = yf.download(fixed_symbol, period=period, interval="1d",
                               progress=False, auto_adjust=False)
            if data is None or data.empty:
                raise ValueError("No data returned")

            data.reset_index(inplace=True)
            data.columns = [c if not isinstance(c, tuple) else c[0] for c in data.columns]

            required_cols = {"Open", "High", "Low", "Close", "Volume", "Date"}
            if not required_cols.issubset(data.columns):
                raise ValueError(f"Missing columns: {required_cols - set(data.columns)}")

            data["Date"] = pd.to_datetime(data["Date"]).dt.date
            data = data[["Date", "Open", "High", "Low", "Close", "Volume"]]
            return data

        except Exception as e:
            print(f"⚠️ Error fetching {symbol}: {e}")
            time.sleep(1.5 * attempt + random.random())

    print(f"❌ {symbol} failed after {MAX_RETRY} retries.")
    return pd.DataFrame()

# ---------------------------------------
# 🧩 主流程
# ---------------------------------------
def main(full_mode=False):
    """
    full_mode=True  → 全量抓 1 年資料
    full_mode=False → 每日自動補近 5 日
    """
    period = "1y" if full_mode else "5d"
    print(f"\n🚀 開始抓取 Yahoo Finance ({period}) ...\n")

    # 建立表結構（如未存在）
    with engine.begin() as conn:
        conn.execute(text("""
            CREATE TABLE IF NOT EXISTS stock_ohlc (
                id SERIAL PRIMARY KEY,
                symbol VARCHAR(10) REFERENCES stock_symbols(symbol) ON DELETE CASCADE,
                date DATE NOT NULL,
                open DOUBLE PRECISION,
                high DOUBLE PRECISION,
                low DOUBLE PRECISION,
                close DOUBLE PRECISION,
                volume BIGINT,
                CONSTRAINT unique_symbol_date UNIQUE (symbol, date)
            );
        """))

        # 如有 failed log，優先續跑
        if os.path.exists(FAILED_FILE):
            failed_list = [s.strip() for s in open(FAILED_FILE) if s.strip()]
            print(f"🔁 續跑上次失敗清單：{len(failed_list)} 隻股票。")
            symbols = failed_list
        else:
            symbols_df = pd.read_sql("SELECT symbol FROM stock_symbols;", conn)
            symbols = symbols_df["symbol"].tolist()
            print(f"📈 Total symbols: {len(symbols)}")

    failed_symbols = []

    with engine.begin() as conn:
        for sym in symbols:
            df = fetch_yahoo_ohlc(sym, period=period)
            if df.empty:
                failed_symbols.append(sym)
                continue

            try:
                # 刪除近 5 天舊資料（避免重複）
                if not full_mode:
                    conn.execute(text("""
                        DELETE FROM stock_ohlc
                        WHERE symbol = :symbol
                        AND date >= CURRENT_DATE - INTERVAL '5 days';
                    """), {"symbol": sym})

                df["symbol"] = sym
                df.rename(columns={
                    "Date": "date",
                    "Open": "open",
                    "High": "high",
                    "Low": "low",
                    "Close": "close",
                    "Volume": "volume"
                }, inplace=True)

                df.to_sql("stock_ohlc", conn, if_exists="append", index=False, method="multi")
                print(f"✅ {sym} done ({len(df)} rows)")

            except Exception as e:
                print(f"⚠️ Insert failed for {sym}: {e}")
                failed_symbols.append(sym)

            time.sleep(2.5)  # Yahoo API 限速保護

    # -----------------------------------
    # 💾 儲存失敗清單
    # -----------------------------------
    if failed_symbols:
        with open(FAILED_FILE, "w") as f:
            f.write("\n".join(failed_symbols))
        print(f"\n⚠️ 共 {len(failed_symbols)} 隻失敗，已記錄到 {FAILED_FILE}。")
    else:
        if os.path.exists(FAILED_FILE):
            os.remove(FAILED_FILE)
        print("\n🎉 全部成功！已清除 failed log。")

    print(f"✅ 完成 {period} 抓取！PostgreSQL 已更新。")

# ---------------------------------------
if __name__ == "__main__":
    # full_mode=True → 初次全量抓一年
    # full_mode=False → 每日排程自動補近5日
    main(full_mode=False)