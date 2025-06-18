import pandas as pd
from datetime import date, timedelta
import savant_scraper

def create_game_dates():
    i = 1

    day = date(2025, 3, 27)

    dates = []

    while day.month != 6 or day.day != 5:
        print(day)
        dates.append([i, day.month, day.day, day.year])
        i += 1
        day = day + timedelta(days=1)

    dates_df = pd.DataFrame(dates, columns=["id", "month", "day", "year"])

    dates_df.to_csv("Game Data/game_dates.csv", index=False)


def day_offset(days=30):
    today = date.today()

    day = today + timedelta(days=-days)

    main_df = pd.read_csv("Game Data/ev_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv")
    main_df2 = pd.read_csv("Game Data/pitch_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv")

    day = day + timedelta(days=1)

    while day != today:
        df = pd.read_csv("Game Data/ev_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv")
        df2 = pd.read_csv("Game Data/pitch_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv")

        main_df = pd.concat([main_df, df], ignore_index=True)
        main_df2 = pd.concat([main_df2, df2], ignore_index=True)
        
        day = day + timedelta(days=1)

    main_df.to_csv('Game Data/' + str(days) + '_day_ev.csv', index=False)
    main_df2.to_csv('Game Data/' + str(days) + '_day_pitch.csv', index=False)

    print(pd.read_csv('Game Data/' + str(days) + '_day_ev.csv'))
    print(pd.read_csv('Game Data/' + str(days) + '_day_pitch.csv'))


df = pd.read_csv("Game Data/pitch_data2025-6-5.csv")
df_ev = pd.read_csv("Game Data/ev_data2025-6-5.csv")

df2 = df[df['Batter'] == 'Taylor Walls']
ev_df2 = df_ev[df_ev['Batter'] == 'Taylor Walls']

print(df2[['Batter', 'Pitcher', 'PA']])
print(ev_df2)

jack_leiter1 = df[df['Pitcher'] == 'Jack Leiter']
jack_leiter2 = df[df['Pitcher'] == 'Jack Leiter']

print(set(jack_leiter1['Result'].to_list()))

day_offset(60)
day_offset(30)
day_offset(14)
day_offset(7)

