import pandas as pd
import psycopg2

def normalize_symbol(symbol: str) -> str:
    if not isinstance(symbol, str):
        return symbol
    symbol = symbol.strip().upper()

    # 🪄 無論 dash 定點都當成同一間公司
    if "BRK" in symbol:
        return "BRK.A"
    if "BF" in symbol:
        return "BF.B"
    if "-" in symbol:
        symbol = symbol.replace("-", ".")
    return symbol


def load_symbols():
    csv_url = "https://raw.githubusercontent.com/datasets/s-and-p-500-companies/master/data/constituents.csv"
    df = pd.read_csv(csv_url)
    df.columns = [c.strip().lower() for c in df.columns]  # 統一小寫
    print("✅ Loaded", len(df), "rows from CSV")
    print("🧩 Columns found:", list(df.columns))

    conn = psycopg2.connect(
        host="localhost",
        database="stockdb",
        user="postgres",
        password="1234"
    )
    cur = conn.cursor()

    cur.execute("""
        CREATE TABLE IF NOT EXISTS stock_symbols (
            symbol VARCHAR(10) PRIMARY KEY,
            name VARCHAR(255),
            industry VARCHAR(255)
        );
    """)

    inserted = 0
    for _, row in df.iterrows():
        symbol = normalize_symbol(row["symbol"])
        name = row.get("security", "")
        industry = row.get("gics sector", "")

        cur.execute("""
            INSERT INTO stock_symbols (symbol, name, industry)
            VALUES (%s, %s, %s)
            ON CONFLICT (symbol) DO UPDATE SET
                name = EXCLUDED.name,
                industry = EXCLUDED.industry;
        """, (symbol, name, industry))
        inserted += 1

        if row["symbol"].upper() in ["BRK-B", "BF-B"]:
            print(f"🔁 Converted {row['symbol']} → {symbol}")

    conn.commit()
    print("💾 Symbols loaded into database successfully. Total", len(df), "rows.")

    # --- 🧹 補救特例 (放喺最後) ---
    cur.execute("DELETE FROM stock_symbols WHERE symbol IN ('BRK.B', 'BRK-B');")
    cur.execute("""
        INSERT INTO stock_symbols (symbol, name, industry)
        VALUES ('BRK.A', 'Berkshire Hathaway Inc.', 'Financials')
        ON CONFLICT DO NOTHING;
    """)
    conn.commit()
    print("🔧 Fixed special symbols: BRK.A inserted, BRK.B removed.")

    cur.close()
    conn.close()

if __name__ == "__main__":
    load_symbols()
