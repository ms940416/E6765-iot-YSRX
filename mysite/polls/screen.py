import pyupm_i2clcd as lcd
import datetime,time
from pytz import timezone
import mraa,math
B=4275
R0=100000

myLcd = lcd.Jhd1313m1(0, 0x3E, 0x62)
TIMEZONE = timezone('America/New_York')
while True :

	tempSensor = mraa.Aio(1)
	a = float(tempSensor.read())
	R = 1023/a-1
	R = R0*R
	temp = 1/(math.log(R/R0)/B+1/298.15)-273.15
	t=datetime.datetime.now(TIMEZONE)
	myLcd.clear()
	myLcd.setCursor(0,0)
	myLcd.write(str(t.time())[0:8])
	myLcd.setCursor(0x1F,0)
	myLcd.write('temp = '+str(int(temp)))
	time.sleep(1)
