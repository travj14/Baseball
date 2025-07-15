# import email_updater
import savant_scraper
from datetime import date, timedelta
import pandas
import sys

# email = sys.argv[1]
# code = sys.argv[2]

day = date(2025, 7, 3)

while day != date.today():
    df = savant_scraper.get_ev(day.year, day.month, day.day)
    df.to_csv("Game Data/ev_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv", index=False)

    pitch_df = savant_scraper.get_pitch(day.year, day.month, day.day)
    pitch_df.to_csv("Game Data/pitch_data" + savant_scraper.date_format(day.year, day.month, day.day) +".csv", index=False)

    day = day + timedelta(days=1)
    print(day.month, day.day)


# email_updater.send_email(email, code, "hi")





