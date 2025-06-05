from playwright.sync_api import sync_playwright
import requests
from bs4 import BeautifulSoup
import re
import time
import pandas as pd

def date_format(year, month, day):
    return str(year) + "-" + str(month) + "-" + str(day)

def get_ev(year, month, day):
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()
        page.goto("https://baseballsavant.mlb.com/gamefeed?date=" + date_format(year, month, day) + "&chartType=pitch&legendType=pitchName&playerType=pitcher&inning=&count=&pitchHand=&batSide=&descFilter=&ptFilter=&resultFilter=&hf=exitVelocity&sportId=1")

        # page.wait_for_selector("tr[id^='exitVelocityTable_']", timeout=10000)
        time.sleep(10)

        content = page.content()
        
        soup = BeautifulSoup(content, "html.parser")

        #tables = soup.find_all(class_="table-savant")

        metas = soup.find_all("tr", id=re.compile("exitVelocityTable"))

        exit_velo = []

        for row in metas:
            cells = row.find_all(["td", "th"])  # include both headers and data cells
            values = [cell.get_text(strip=True) for cell in cells]
            exit_velo.append(values)
        
        columns = soup.find_all("tr", class_="tr-component-row")

        row = columns[-1]
        cells = row.find_all(["td", "th"])
        ev_header = [cell.get_text(strip=True) for cell in cells]

        ev_header[8] = "EV"
        ev_header[9] = "LA"
        ev_header[10] = "Dist"
        ev_header[11] = "Bat_speed"
        ev_header[12] = "Pitch_velo"
        ev_header[13] = "xBA"
        ev_header[14] = "numParks"

        browser.close()
        return pd.DataFrame(exit_velo, columns = ev_header)
    
def get_pitch(year, month, day):
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

    # for elem in ev_elements:
        # print(elem.name, elem['id'], "-", elem.get_text(strip=True))

    # print(ev_elements)

        page.goto("https://baseballsavant.mlb.com/gamefeed?date=" + date_format(year, month, day) + "&chartType=pitch&legendType=pitchName&playerType=pitcher&inning=&count=&pitchHand=&batSide=&descFilter=&ptFilter=&resultFilter=&hf=pitchVelocity&sportId=1")

        time.sleep(10)

        content = page.content()

        soup = BeautifulSoup(content, "html.parser")

        metas = soup.find_all("tr", id=re.compile("pitchVelocityTable"))

        pitch_velo = []
        
        for row in metas:
            cells = row.find_all(["td", "th"])
            values = [cell.get_text(strip=True) for cell in cells]
            pitch_velo.append(values)

        columns = soup.find_all("tr", class_="tr-component-row")

        row = columns[-1]
        cells = row.find_all(["td", "th"])
        pitch_header = [cell.get_text(strip=True) for cell in cells]

        print(soup.title.string)
        browser.close()

        df = pd.DataFrame(pitch_velo, columns=pitch_header)
        df.columns.values[-2] = "cut/run"

        col_negate = df.columns[-3]
        col_condition = df.columns[-2]

        df[col_negate] = pd.to_numeric(df[col_negate], errors='coerce')

        # Then safely apply the condition
        df.loc[df[col_condition] == "→", col_negate] *= -1

        return df
