import pandas as pd
import yfinance as yf
import time
from sqlalchemy import create_engine, text

def fetch_yahoo_ohlc(symbol: str):
    print(f"📊 Fetching {symbol} ...")
    try:
        # ⚙️ 設定 auto_adjust=False，保留原始價
        data = yf.download(symbol, period="1y", interval="1d", progress=False, auto_adjust=False)
        if data is None or data.empty:
            print(f"⚠️ No data for {symbol}")
            return pd.DataFrame()

        # ✅ 防止 MultiIndex 出錯
        data = data.copy()
        data.reset_index(inplace=True)
        data.columns = [c if not isinstance(c, tuple) else c[0] for c in data.columns]

        # ✅ 確認必要欄位存在
        required_cols = {"Open", "High", "Low", "Close", "Volume", "Date"}
        if not required_cols.issubset(set(data.columns)):
            print(f"❌ Error fetching {symbol}: {list(required_cols - set(data.columns))}")
            return pd.DataFrame()

        # ✅ 強制轉型
        data["Date"] = pd.to_datetime(data["Date"]).dt.date
        data = data[["Date", "Open", "High", "Low", "Close", "Volume"]]
        return data

    except Exception as e:
        print(f"❌ Error fetching {symbol}: {e}")
        return pd.DataFrame()

def main():
    engine = create_engine("postgresql+psycopg2://postgres:1234@localhost/stockdb")

    with engine.connect() as conn:
        # 🧱 確保表存在
        conn.execute(text("""
            CREATE TABLE IF NOT EXISTS stock_ohlc (
                id SERIAL PRIMARY KEY,
                symbol VARCHAR(10) REFERENCES stock_symbols(symbol) ON DELETE CASCADE,
                date DATE NOT NULL,
                open DOUBLE PRECISION,
                high DOUBLE PRECISION,
                low DOUBLE PRECISION,
                close DOUBLE PRECISION,
                volume BIGINT
            );
        """))
        conn.commit()

        # 🔹 取出 symbol
        symbols = pd.read_sql("SELECT symbol FROM stock_symbols;", conn)["symbol"].tolist()
        print(f"📈 Total symbols: {len(symbols)}")

        for sym in symbols:
            df = fetch_yahoo_ohlc(sym)
            if df.empty:
                continue

            inserted_rows = 0
            for _, row in df.iterrows():
                try:
                    conn.execute(text("""
                        INSERT INTO stock_ohlc (symbol, date, open, high, low, close, volume)
                        VALUES (:symbol, :date, :open, :high, :low, :close, :volume)
                        ON CONFLICT DO NOTHING;
                    """), {
                        "symbol": sym,
                        "date": row["Date"],
                        "open": float(row["Open"]),
                        "high": float(row["High"]),
                        "low": float(row["Low"]),
                        "close": float(row["Close"]),
                        "volume": int(row["Volume"]) if not pd.isna(row["Volume"]) else None
                    })
                    inserted_rows += 1
                except Exception as e:
                    print(f"⚠️ Insert failed for {sym}: {e}")

            conn.commit()
            print(f"✅ {sym} done ({inserted_rows} rows)")
            time.sleep(3)

    print("✅ All done!")

if __name__ == "__main__":
    main()