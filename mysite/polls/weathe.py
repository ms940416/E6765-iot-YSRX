import urllib2
import json

def weather():
	#get current lat & lon according to the ip address
	location = urllib2.urlopen('http://freegeoip.net/json')
	location=location.read()
	location = json.loads(location)
	lat = location['latitude']
	lon = location['longitude']
	#print(lat,lon)

	#get the weather situation according to the lat & loc
	weather_url='http://api.openweathermap.org/data/2.5/weather?lat='+str(lat)+'&lon='+str(lon)+'&APPID=21b99a2a839802b17a60a5e2043d60f0'
	response = urllib2.urlopen(weather_url)
	data=response.read()
	data=json.loads(data)
	return data['weather'][0]['description']

if __name__ == '__main__':
	weather=weather()
	print weather



