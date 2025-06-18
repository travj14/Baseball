import pandas as pd

slg_mapping = {
    'Single': 1,
    'Double': 2,
    'Triple': 3,
    'Home Run': 4
}

woba_mapping = {
    'Single' : .89, 
    'Double' : 1.27,
    'Triple' : 1.62,
    'Home Run' : 2.1, 
    'Walk' : .69, 
    'Hit By Pitch' : .72
}

singles = { 'single': 0,
    'Double': 0,
    'Triple': 0,
    'Home Run': 1}


df_60 = pd.read_csv("Game Data/60_day_ev.csv")
df_30 = pd.read_csv("Game Data/60_day_ev.csv")
df_14 = pd.read_csv("Game Data/60_day_ev.csv")
df_7 = pd.read_csv("Game Data/60_day_ev.csv")

def summarize(df):
    print(df)

    df = df[~df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Catcher Interference', 'Intent Walk', 'Pickoff 1B', 'Runner Out', 'Caught Stealing 3B', 'Wild Pitch', 'Pickoff Error 1B'])]
    df['Result'] = df['Result'].map(singles).fillna(0.0).astype(float)

    print(df)

    print(df.groupby('Batter').agg(
        PA=('PA', 'count'),
        woba=('Result', 'sum')).sort_values("woba", ascending=False))
    print(df.groupby('Batter')['Result'].mean().sort_values(ascending=False))

    return df

print(set(df_60['Result'].tolist()))

summarize(df_60)

