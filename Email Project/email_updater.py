import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.application import MIMEApplication
import os
import sys


def send_email(email, password):
    print(email)
    print(password)
    sender_email = email
    receiver_email = email
    password = password
    filepath = "display.html"

    message = MIMEMultipart("alternative")
    message["Subject"] = "Daily Dose of Hitting"
    message["From"] = sender_email
    message["To"] = receiver_email

    # text = "Hi, this is a test email sent to myself using Python."
    # message.attach(MIMEText(text, "plain"))

    with open(filepath, "rb") as f:
        part = MIMEApplication(f.read(), Name=os.path.basename(filepath))
        part['Content-Disposition'] = f'attachment; filename="{os.path.basename(filepath)}"'
        message.attach(part)

    with smtplib.SMTP_SSL("smtp.gmail.com", 465) as server:
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, message.as_string())

    print("Email sent successfully.")

send_email(sys.argv[1], sys.argv[2])