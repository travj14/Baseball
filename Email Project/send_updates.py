import email_updater
import savant_scraper
from datetime import date, timedelta
import pandas as pd
import sys

# email = sys.argv[1]
# code = sys.argv[2]

day = date.today() - timedelta(days=1)

year = day.year
month = day.month
day = day.day

df = savant_scraper.get_ev(year, month, day)
df.to_csv("Game Data/ev_data" + savant_scraper.date_format(year, month, day) + ".csv", index=False)

pitch_df = savant_scraper.get_pitch(year, month, day)
pitch_df.to_csv("Game Data/pitch_data" + savant_scraper.date_format(year, month, day) +".csv", index=False)


# email_updater.send_email(email, code, "hi")





