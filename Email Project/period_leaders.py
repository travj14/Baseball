from datetime import date, timedelta
import pandas as pd
import savant_scraper
from bs4 import BeautifulSoup
from comparing_periods import summarize

mapping = {
    'Single': 1,
    'Double': 2,
    'Triple': 3,
    'Home Run': 4
}
mapping2 = {
        'Single' : .89, 
        'Double' : 1.27,
        'Triple' : 1.62,
        'Home Run' : 2.1, 
        'Walk' : .69, 
        'Hit By Pitch' : .72, 
        'Intent Walk' : .69
}

day = date.today() + timedelta(days=-1)

def change_html(soup, id, df, roundTo=3):
        table = soup.find(id=id)

        new_row = soup.new_tag("tbody")

        for index, row in df.iterrows():
            new_row = soup.new_tag("tr")
            
            # Add Batter (index)
            cell1 = soup.new_tag("td")
            cell1.string = str(index)
            new_row.append(cell1)

            cell2 = soup.new_tag("td")
            cell2.string = str(round(row[0]))
            new_row.append(cell2)

            columns = len(row)

            for i in range(columns - 1):
                cell3 = soup.new_tag("td")
                cell3.string = str(round(row[i + 1], roundTo))
                new_row.append(cell3)

            
            # Add wOBA (rounded)

            table.append(new_row)

        return soup

def period_leaders(soup, period_length):

    ev_filename = "Game Data/" + str(period_length) + "_day_ev.csv"
    pitch_filename = "Game Data/" + str(period_length) + "_day_pitch.csv"

    ev_df = pd.read_csv(ev_filename)
    pitch_df = pd.read_csv(pitch_filename)

    print(set(ev_df['Result'].tolist()))
    new_ev_df = ev_df[~ev_df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Catcher Interference'])]

    new_ev_df['Result'] = new_ev_df['Result'].map(mapping2).fillna(0.0).astype(float)

    wobas = new_ev_df.groupby(['id', 'Batter']).agg(
        PA=('PA', 'count'),
        woba=('Result', 'mean'))

    # print(wobas)

    wobas = wobas[wobas['PA'] > period_length * 1.5].sort_values("woba", ascending=False)

    # print('wobas')

    listed_wobas = wobas

    # print(wobas.head(10))

    ev_df['Result'] = ev_df['Result'].map(mapping).fillna(0).astype(int)

    pas = ev_df.groupby(['id', 'Batter'])['PA'].count()
    pas = pas[pas > 2]

    hard_hit_df = ev_df[ev_df['EV'] >= 95]

    hard_hits = hard_hit_df.groupby(['id', 'Batter'])['PA'].count()

    listed_hard_hit = hard_hits.sort_values(ascending=False)
    hard_hit_rate = round(100 * (hard_hits / pas))

    # Combine into a DataFrame and include pas
    hard_hit_rate = pd.DataFrame({
        'PA': pas,
        'Hard Hit Rate': hard_hit_rate
    }).dropna()

    hard_hit_rate = hard_hit_rate.sort_values('Hard Hit Rate', ascending=False)

    listed_hard_rate = hard_hit_rate

    tbs = ev_df.groupby(['id', 'Batter'])['Result'].sum().sort_values(ascending=False)

    listed_tbs = pd.DataFrame({
        'PA': pas,
        'tbs': tbs
    }).sort_values("tbs", ascending=False)

    ev_df['Result'] = ev_df['Result'].apply(lambda x: 1 if x != 0 else 0)

    # with open("display.html", "r") as f:
        # soup = BeautifulSoup(f, "html.parser")

    listed_wobas = listed_wobas.droplevel('id')
    listed_hard_hit = listed_hard_hit.droplevel('id').head(10)
    listed_hard_rate = listed_hard_rate.droplevel('id')
    listed_tbs = listed_tbs.droplevel('id').head(10)

    listed_wobas = listed_wobas[listed_wobas['PA'] >= 1.5 * period_length].head(10)
    listed_hard_rate = listed_hard_rate[listed_hard_rate['PA'] >= 1.5 * period_length].head(10)

    soup = change_html(soup, str(period_length) + " woba", listed_wobas)

    listed_hard_hit = listed_hard_hit.reset_index(name='PA')
    listed_hard_hit.set_index('Batter', inplace=True)

    soup = change_html(soup, str(period_length) + " hardHit", listed_hard_hit)

    soup = change_html(soup, str(period_length) + " hardRate", listed_hard_rate, None)

    soup = change_html(soup, str(period_length) + " tbs", listed_tbs, None)

    return soup

    # Save the modified HTML
    with open("display.html", "w") as f:
        f.write(str(soup))
        return soup
        print("hi")

def period_risers(soup, period_length):
    if period_length == 7:
        first_period = 14
    elif period_length == 14:
        first_period = 30
    elif period_length == 30:
        first_period = 60
    else:
        raise ValueError("Wrong input dipshit")
    
    ev_filename = "Game Data/" + str(period_length) + "_day_ev.csv"
    ev2_filename = "Game Data/" + str(first_period) + "_day_ev.csv"

    df = pd.read_csv(ev_filename)
    df2 = pd.read_csv(ev2_filename)

    woba_df = df[~df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Catcher Interference'])]
    woba_df2 = df2[~df2['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Catcher Interference'])]

    woba_df['Result'] = woba_df['Result'].map(mapping2).fillna(0.0).astype(float)
    woba_df2['Result'] = woba_df2['Result'].map(mapping2).fillna(0.0).astype(float)

    woba1 = woba_df.groupby(['id', 'Batter']).agg(
        PA=('PA', 'count'),
        woba=('Result', 'mean'))
    woba2 = woba_df2.groupby(['id', 'Batter']).agg(
        PA=('PA', 'count'),
        woba=('Result', 'mean'))
    
    woba1 = woba1[woba1['PA'] > period_length * 1.5]

    woba_improvers = pd.DataFrame({
    'woba_change': woba1['woba'] - woba2['woba'],
    'new_woba': woba1['woba'],
    'current_pa': woba1['PA']
    })
    listed_woba = woba_improvers.sort_values(by="woba_change", ascending=False)

    df['Result'] = df['Result'].map(mapping).fillna(0.0).astype(float)
    df2['Result'] = df2['Result'].map(mapping).fillna(0.0).astype(float)

    pas = df.groupby(["id", "Batter"])['PA'].count()
    hard_hits = df[df['EV'] >= 95].groupby(["id", "Batter"])['PA'].count()

    print(pas)
    print(hard_hits)

    hard_hit_rate = pd.DataFrame({
        'hard_hit': hard_hits,
        'rate': hard_hits / pas,
        'PA': pas
    })

    hard_hit_rate = hard_hit_rate[hard_hit_rate["PA"] >= 1.5 * period_length]

    bases = df.groupby(["id", "Batter"])['Result'].sum()
    
    tbs = pd.DataFrame({
        'bases': bases,
        'PA': pas
    })

    print(hard_hit_rate)
    print()

def period_comparison(soup, period_length):
    if period_length == 30:
        df = pd.read_csv("Game Data/30_day_ev.csv")
        df2 = pd.read_csv("Game Data/60_day_ev.csv")
    elif period_length == 14:
        df = pd.read_csv("Game Data/14_day_ev.csv")
        df2 = pd.read_csv("Game Data/30_day_ev.csv")
    else:
        raise ValueError("Wrong input dipshit")
    
    df = summarize(df)
    df2 = summarize(df2)

    merged = pd.DataFrame({
        "Woba Diff": df["woba"] - df2["woba"],
        "Avg Diff": df["avg"] - df2["avg"],
        "Obp Diff": df["obp"] - df2["obp"],
        "Slg Diff": df["slg"] - df2["slg"]
    })
    merged = df.merge(merged, left_index=True, right_index=True)

    merged = merged.droplevel('id')

    soup = change_html(soup, str(period_length) + "-movers", merged)

    return soup






period_risers(None, 7)

    






