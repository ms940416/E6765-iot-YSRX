# E6765-iot-YSRX
Smart Clock project for E6765 at Columbia University
Team YSRX

This repository contains 2 folder: one is the web sever built in Intel Edison called mysite, the other is one Android app.
The fuctions such project can realize is the follwing:
1. Make the clock read the events from google calendar automatically, and caculate the leaving time from the current place according to
the traffic condition and the dictance from the place here to the destination.
2. Apply the function mentioned above to specific situation, i.e. morning wake up.
3. Make the clock able to understand conditional command. e.g. wake me up at 5 a.m. if next morning is sunny, otherwise 7 a.m.
4. Build a sensor system which can give advice about the current environment, e.g. too hot for sleep.

The mysite folder is a Django framework which can communicate with multiple APIs
The Android folder is one app which provide the UI for user to control this clock
