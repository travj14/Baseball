from datetime import date, timedelta
import pandas as pd
import savant_scraper
from bs4 import BeautifulSoup
from period_leaders import period_leaders

pd.set_option("display.float_format", "{:.5f}".format)

mapping = {
    'Single': 1,
    'Double': 2,
    'Triple': 3,
    'Home Run': 4
}

day = date.today() + timedelta(days=-1)

ev_filename = "Game Data/ev_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv"
pitch_filename = "Game Data/pitch_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv"

ev_df = pd.read_csv(ev_filename)
pitch_df = pd.read_csv(pitch_filename)

print(set(ev_df['Result'].tolist()))
new_ev_df = ev_df[~ev_df['Result'].isin(['Caught Stealing 2B', 'Sac Bunt', 'Sac Fly', 'Catcher Interference'])]

mapping2 = {
    'Single' : .89, 
    'Double' : 1.27,
    'Triple' : 1.62,
    'Home Run' : 2.1, 
    'Walk' : .69, 
    'Hit By Pitch' : .72, 
    'Intent Walk' : .69
}

new_ev_df['Result'] = new_ev_df['Result'].map(mapping2).fillna(0.0).astype(float)

wobas = new_ev_df.groupby(['id', 'Batter']).agg(
    PA=('PA', 'count'),
    woba=('Result', 'mean'))

# print(wobas)

wobas = wobas[wobas['PA'] > 2].sort_values("woba", ascending=False)

# print('wobas')

listed_wobas = wobas.head(10)

# print(wobas.head(10))

ev_df['Result'] = ev_df['Result'].map(mapping).fillna(0).astype(int)

pas = ev_df.groupby(['id', 'Batter'])['PA'].count()
pas = pas[pas > 2]

hard_hit_df = ev_df[ev_df['EV'] >= 95]


#print('Hard hit df')
#print(hard_hit_df)

#print(hard_hit_df[hard_hit_df['Batter'] == "Max Muncy"])

hard_hits = hard_hit_df.groupby(['id', 'Batter'])['PA'].count()

#print(hard_hits)
#print(pas)

# print('Leaders in hard hit balls')

listed_hard_hit = hard_hits.sort_values(ascending=False).head(10)

# print(hard_hits.sort_values(ascending=False).head(10))

#print(pas)

#print(hard_hits)

hard_hit_rate = round(100 * (hard_hits / pas))

# Combine into a DataFrame and include pas
hard_hit_rate = pd.DataFrame({
    'PA': pas,
    'Hard Hit Rate': hard_hit_rate
}).dropna()

hard_hit_rate = hard_hit_rate.sort_values('Hard Hit Rate', ascending=False)

# print('Leaders in hard hit rate')

listed_hard_rate = hard_hit_rate.head(10)

# print(hard_hit_rate.head(10))

tbs = ev_df.groupby(['id', 'Batter'])['Result'].sum().sort_values(ascending=False)

# print('Leaders in total bases')

listed_tbs = pd.DataFrame({
    'PA': pas,
    'tbs': tbs
}).sort_values("tbs", ascending=False).head(10)

# print(tbs.head(10))

ev_df['Result'] = ev_df['Result'].apply(lambda x: 1 if x != 0 else 0)

# print(ev_df[ev_df['Batter'] == 'Wyatt Langford'])

# print(wobas.loc["Tyler Soderstrom", 'woba'])

with open("display_base.html", "r") as f:
    soup = BeautifulSoup(f, "html.parser")


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

print(listed_wobas)
print(listed_hard_hit)
print(listed_hard_rate)
print(tbs)

listed_wobas = listed_wobas.droplevel('id')
listed_hard_hit = listed_hard_hit.droplevel('id')
listed_hard_rate = listed_hard_rate.droplevel('id')
listed_tbs = listed_tbs.droplevel('id')


soup = change_html(soup, "daily woba", listed_wobas)

listed_hard_hit = listed_hard_hit.reset_index(name='PA')
listed_hard_hit.set_index('Batter', inplace=True)

# print(listed_hard_hit)

soup = change_html(soup, "daily hardHit", listed_hard_hit)

soup = change_html(soup, "daily hardRate", listed_hard_rate, None)

soup = change_html(soup, "daily tbs", listed_tbs, None)

soup = period_leaders(soup, 7)
soup = period_leaders(soup, 14)
soup = period_leaders(soup, 30)
soup = period_leaders(soup, 60)

# Save the modified HTML
with open("display.html", "w") as f:
    f.write(str(soup))
    print("hi")

