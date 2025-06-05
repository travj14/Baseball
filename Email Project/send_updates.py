import email_updater
import savant_scraper
from datetime import date, timedelta
import pandas
import sys

email = sys.argv[1]
code = sys.argv[2]

day = date.today()

print(day.year)
print(day.month)
print(day.day)

df = savant_scraper.get_ev(2025, 6, 3)
df.to_csv("ev_data" + savant_scraper.date_format(day.year, day.month, day.day) + ".csv", index=False)

pitch_df = savant_scraper.get_pitch(2025, 6, 3)
pitch_df.to_csv("pitch_data" + savant_scraper.date_format(day.year, day.month, day.day) +".csv", index=False)

print(df)
print(pitch_df)

email_updater.send_email(email, code, "hi")





