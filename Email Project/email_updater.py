import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart


def send_email(email, password, message):
    sender_email = email
    receiver_email = email
    password = password

    message = MIMEMultipart("alternative")
    message["Subject"] = "Test Email to Self"
    message["From"] = sender_email
    message["To"] = receiver_email

    text = "Hi, this is a test email sent to myself using Python."
    message.attach(MIMEText(text, "plain"))

    with smtplib.SMTP_SSL("smtp.gmail.com", 465) as server:
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, message.as_string())

    print("Email sent successfully.")