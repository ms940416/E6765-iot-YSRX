#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#edison_google_calendar.py
#    Part of "Intel IoT Edison Google Calendar reminder"
#    Copyright 2014 Pavlos Iliopoulos, techprolet.com
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
# 
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
# 
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.


import sys

from oauth2client import client
from googleapiclient import sample_tools


import sched,time
from datetime import datetime, timedelta

checkEvery = 10; #seconds
args = [];

def checkCalendar(sc): 
    
  # Authenticate and construct service.
  service, flags = sample_tools.init(
      args, 'calendar', 'v3', __doc__, __file__,
      scope='https://www.googleapis.com/auth/calendar.readonly')

  try:
#    page_token = None
#    while True:
#      calendar_list = service.calendarList().list(pageToken=page_token).execute()
#      for calendar_list_entry in calendar_list['items']:
#        print calendar_list_entry['summary']
#      page_token = calendar_list.get('nextPageToken')
#      if not page_token:
#        break
    events = service.events().list(calendarId='primary',
    	    		      timeMin = datetime.utcnow().strftime("%Y-%m-%dT%H:%M:%SZ"), # current GMT
                              timeMax = (datetime.utcnow() + timedelta(minutes=5)).strftime("%Y-%m-%dT%H:%M:%SZ") #plus five minutes
                              ).execute()
    print (datetime.utcnow() + timedelta(minutes=5)).strftime("%Y-%m-%dT%H:%M:%SZ")
    arduinoFile = open('/tmp/arduino.txt', 'w+')
    if len(events["items"])==0:
        print >>arduinoFile, " "
        print >>arduinoFile, " "
        print >>arduinoFile, "OK"
    else:
    	print len(events["items"]), "events"
    for event in events["items"]:
        print >>arduinoFile, event["summary"]
        if 'location' in event.keys():
        	print >>arduinoFile, event["location"]
        else:
        	print >>arduinoFile, " "
        print >>arduinoFile, "OK"
    arduinoFile.close()
    print "++++++++++++++++++++++++++"
  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run'
      'the application to re-authorize.')

  sc.enter(checkEvery, 1, checkCalendar, (sc,))
    
def main(argv):
	
	  
  # Authenticate and construct service.
  service, flags = sample_tools.init(
      argv, 'calendar', 'v3', __doc__, __file__,
      scope='https://www.googleapis.com/auth/calendar.readonly')

	
	
	
  args = argv;
  s = sched.scheduler(time.time, time.sleep)


  s.enter(1, 1, checkCalendar, (s,))
  s.run()
  
  
if __name__ == '__main__':
  main(sys.argv)

