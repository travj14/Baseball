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

obp_mapping = {
    'Single' : 1, 
    'Double' : 1,
    'Triple' : 1,
    'Home Run' : 1, 
    'Walk' : 1, 
    'Hit By Pitch' : 1
}

avg_map = { 1: 1,
    2: 1,
    3: 1,
    4: 1}

k_map =  { 'Strikeout': 1}




df_60 = pd.read_csv("Game Data/60_day_ev.csv")
df_30 = pd.read_csv("Game Data/30_day_ev.csv")
df_14 = pd.read_csv("Game Data/14_day_ev.csv")
# df_7 = pd.read_csv("Game Data/60_day_ev.csv")

def summarize(df):

    hard_hits = df[df["EV"] >= 95]
    hard_hits_count = hard_hits.groupby(["id", "Batter"])['PA'].count()
    pas = df.groupby(["id", "Batter"])['PA'].count()
    
    hard_hit_df = pd.DataFrame({
        'hard_hits': hard_hits_count,
        'PA': pas
    }).fillna(0)

    hard_hit_df['hard_hit_rate'] = hard_hit_df['hard_hits'] / hard_hit_df['PA']
    hard_hit_df = hard_hit_df[['hard_hit_rate']]

    woba_df = df[~df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Sac Fly Double Play', 'Runner Out',
                                    'Catcher Interference', 'Intent Walk', 'Pickoff 1B', 'Pickoff 3B', 'Pickoff Error 1B',
                                    'Runner Out', 'Caught Stealing 3B', 'Wild Pitch', 'Pickoff Error 1B', 'Stolen Base 2B',
                                    'Pickoff Caught Stealing 3B', 'Pickoff Caught Stealing 2B', 'Pickoff 2B', 'Pickoff 3B'])]
    woba_df['Result'] = woba_df['Result'].map(woba_mapping).fillna(0.0).astype(float)

    print(woba_df)

    woba = woba_df.groupby(['id', 'Batter']).agg(
        PA=('PA', 'count'),
        woba=('Result', 'mean')).sort_values("PA", ascending=False)
    print(woba_df.groupby('Batter')['Result'].mean().sort_values(ascending=False))

    

    ab_df = df[~df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Sac Fly Double Play', 'Runner Out',
                                    'Catcher Interference', 'Intent Walk', 'Pickoff 1B', 'Pickoff 3B', 'Pickoff Error 1B',
                                    'Runner Out', 'Caught Stealing 3B', 'Wild Pitch', 'Pickoff Error 1B', 'Stolen Base 2B',
                                    'Pickoff Caught Stealing 3B', 'Pickoff Caught Stealing 2B', 'Pickoff 2B', 'Pickoff 3B', 'Walk', 'Hit By Pitch'])]

    slg_df = ab_df
    slg_df['Result'] = slg_df['Result'].map(slg_mapping).fillna(0.0).astype(float)

    slg = slg_df.groupby(['id', 'Batter']).agg(
        AB=('PA', 'count'),
        slg=('Result', 'mean')).sort_values("AB", ascending=False)
    
    slg_df['Result'] = slg_df['Result'].map(avg_map).fillna(0.0).astype(float)
    avg = slg_df.groupby(['id', 'Batter']).agg(
        AB=('PA', 'count'),
        avg=('Result', 'mean')).sort_values("AB", ascending=False)
    
    print(slg)
    print(avg)

    obp_df = df[~df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Sac Fly Double Play', 'Runner Out',
                                    'Catcher Interference', 'Intent Walk', 'Pickoff 1B', 'Pickoff 3B', 'Pickoff Error 1B',
                                    'Runner Out', 'Caught Stealing 3B', 'Wild Pitch', 'Pickoff Error 1B', 'Stolen Base 2B',
                                    'Pickoff Caught Stealing 3B', 'Pickoff Caught Stealing 2B', 'Pickoff 2B', 'Pickoff 3B'])]
    obp_df['Result'] = obp_df['Result'].map(woba_mapping).fillna(0.0).astype(float)

    obp = obp_df.groupby(['id', 'Batter']).agg(
        PA=('PA', 'count'),
        obp=('Result', 'mean')
    ).sort_values('PA', ascending=False)

    k_df = df[~df['Result'].isin(['Caught Stealing 2B', 'Runner Out',
                                    'Pickoff 1B', 'Pickoff 3B', 'Pickoff Error 1B',
                                    'Caught Stealing 3B', 'Wild Pitch', 'Pickoff Error 1B', 'Stolen Base 2B',
                                    'Pickoff Caught Stealing 3B', 'Pickoff Caught Stealing 2B', 'Pickoff 2B', 'Pickoff 3B'])]
    k_df['Result'] = k_df['Result'].map(k_map).fillna(0).astype(int)

    k = k_df.groupby(['id', 'Batter']).agg(
        k_rate = ('Result', 'mean')
    )

    print(hard_hit_df)
    print(k)

    merged = woba.merge(slg, left_index=True, right_index=True)
    merged = merged.merge(avg, left_index=True, right_index=True)
    merged = merged.merge(obp, left_index=True, right_index=True)
    merged = merged.drop(columns=['PA_y', 'AB_y'])
    merged = merged.rename(columns={
        'PA_x': 'PA',
        'AB_x': 'AB'
    })
    merged = merged.merge(k, left_index=True, right_index=True)
    merged = merged.merge(hard_hit_df, left_index=True, right_index=True)

    print(merged)
    merged = merged[['PA', 'AB', 'woba', 'avg', 'obp', 'slg', 'k_rate', 'hard_hit_rate']]

    return merged



print(set(df_60['Result'].tolist()))

summarize(df_30)

