Worldwide Windsurfer's Weather Service

This simple app was created to help people choose destination for windsurfing trip in given date based on Weatherbit Forecast.

User can simply write a request using for example postman containing preferred date and application will automatically get forecast from Weatherbit API and response with the best location based on this formula for calculation points:

wind speed *3 + temperature

If wind speed is not between 5 and 18 or temperature is not between 5 and 35 no points will be given for that location.

Additionally, user can simply edit list of locations using Enum.

How to run:

clone this repo form GitHub,
use your favourite IDE to run all tests and then run the application,
using HTTP client send a GET request with date on address :

http://localhost:8080/forecast?dateString=2023-04-03

if you want to add or remove locations just edit CitiesEnum
Thanks for downloading!