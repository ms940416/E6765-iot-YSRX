from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
# Create your views here.
from django.http import HttpResponse
from django.template import loader
from .models import Question
tempdata=[]
count=0
import datetime
import mraa
import time
import weathe
import pyupm_i2clcd as lcd
import math
import json
import urllib2
from getcalendar import get_secrets
from pytz import timezone
from threading import Thread
TIMEZONE = timezone('America/New_York')
homeaddr="Columbia_University"

def jogging(message,buzz,switch):    
    goodtime=message['0']
    goodhours, goodminutes = map(int, goodtime.split(':'))
    badtime=message['1']
    badhours, badminutes = map(int, badtime.split(':'))
    nytime=datetime.datetime.fromtimestamp(time.time(),TIMEZONE)
    if int(nytime.hour)>badhours:
        callday=nytime.day+1
    else:
        callday=nytime.day
    goodcalltime=datetime.datetime(nytime.year,nytime.month,callday,goodhours,goodminutes,0,0) 
    badcalltime=datetime.datetime(nytime.year,nytime.month,callday,badhours,badminutes,0,0)
    goodcalltime=TIMEZONE.localize(goodcalltime)
    badcalltime=TIMEZONE.localize(badcalltime)
    print goodcalltime
    print badcalltime
    print nytime
    while True:
        weather=weathe.weather()
        nytime=datetime.datetime.fromtimestamp(time.time(),TIMEZONE)
        #print nytime
        #print nytime>goodcalltime
        #print nytime>badcalltime
        if nytime>goodcalltime and nytime<badcalltime:
            print weather
            if weather=="clear sky" or weather=="few clouds":
                buzz.write(1)
                while not switch.read():
       	       	    pass
                buzz.write(0)
                return
            else:
                goodcalltime=badcalltime
        elif nytime>badcalltime:
            buzz.write(1)
            while not switch.read():
                pass
            buzz.write(0)
            return
        time.sleep(5)

@csrf_exempt
def lighton(request):
    message=json.loads(request.body)
    #print message
    buzz_pin_number=4
    buzz = mraa.Gpio(buzz_pin_number)
    buzz.dir(mraa.DIR_OUT)
    switch_pin_number=8
    switch = mraa.Gpio(switch_pin_number)
    switch.dir(mraa.DIR_IN)
    t1=Thread(target=jogging,args=[message,buzz,switch])
    t1.daemon=True
    t1.start()    
    # myLcd = lcd.Jhd1313m1(0, 0x3E, 0x62)
    # myLcd.setCursor(0,0)
    # myLcd.clear()
    # myLcd.write("test")
    time.sleep(2)

    return HttpResponse("light on")

@csrf_exempt
def sleep(request):
    global homeaddr
    print homeaddr
    buzz_pin_number=4
    buzz = mraa.Gpio(buzz_pin_number)
    buzz.dir(mraa.DIR_OUT)
    switch_pin_number=8
    switch = mraa.Gpio(switch_pin_number)
    switch.dir(mraa.DIR_IN)
    from getcalendar import get_secrets
    starttime,location=get_secrets()
    location=location.replace(" ","_")
    preparetime=1800
    advancetime=600
    response = urllib2.urlopen('https://maps.googleapis.com/maps/api/distancematrix/json?origins='+homeaddr+'&destinations=%s&mode=transit&key=AIzaSyCtuHQw7BbPzM6S0y796eQ6vtULxvECCD4' %(location))
    data=response.read()
    data=json.loads(data)
    duration=data["rows"][0]["elements"][0]["duration"]["value"]    
    #starttime=TIMEZONE.localize(datetime.datetime.fromtimestamp(starttime))
    rest=starttime-(time.time()+duration+preparetime+advancetime)+18000
    #rest=TIMEZONE.localize(datetime.datetime.fromtimestamp(time.time()))
    if rest<0:
        buzz.write(1)
        while not switch.read():
            pass
        buzz.write(0) 
        return HttpResponse(0)
    hour=int(rest/3600)
    rest=rest-hour*3600
    minute=int(rest/60)
    return HttpResponse(str(hour).zfill(2)+":"+str(minute).zfill(2))

@csrf_exempt
def address(request):
    global homeaddr
    message=json.loads(request.body)["HomeAdd"]
    homeaddr=message.replace(" ","_")
    return HttpResponse()
    

def readdata():
    global count
    import mraa,math
    B=4275
    R0=100000
    tempSensor = mraa.Aio(1)
    a = float(tempSensor.read())
    #print a
    R = 1023/a-1
    #print R
    R = R0*R
    temp = 1/(math.log(R/R0)/B+1/298.15)-273.15
    count=count+1
    return [count,temp]

def index(request):
    global tempdata
    #template = loader.get_template('polls/index.html')
    #return HttpResponse(template.render(request))
    #return HttpResponse("Hello, world. You're at the polls index.")
    #return render(request, 'polls/index.html')
    #latest_question_list = Question.objects.order_by('-pub_date')[:5]
    tempdata.append(readdata())
    return render(request, 'polls/index.html',{'content':tempdata})

@csrf_exempt
def environment(request):
    B=4275
    R0=100000
    lightSensor = mraa.Aio(0)
    a = float(lightSensor.read())
    tempSensor = mraa.Aio(1)
    b = float(tempSensor.read())
    R = 1023/b-1
    R = R0*R
    b = 1/(math.log(R/R0)/B+1/298.15)-273.15
    soundSensor = mraa.Aio(2)
    c = float(soundSensor.read())
    result=""
    if a>300:
        result+="Turn down the light."
    if b>26:
        result+="Turn down the temperature."
    if b<22:
        result+="Turn up the temperature."
    print c
    if c<200:
        result+="Turn down the sound."
    if result=="":
        result="Good environment."
    return HttpResponse(result)
