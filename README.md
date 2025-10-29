## Weather App
![Design sem nome (1)](https://github.com/estevao-souza/WeatherAppProject/assets/62207482/d2dbf70c-7ef7-4f85-b372-f52dd377aacb)

### About this project
Weather was created with the aim of practicing my knowledge in developing Android applications with Kotlin that consumes an external API but also focuses on layout. This application uses the MVVM architecture pattern and consumes 2 OpenWeatherMap APIs, so that different types of information are presented, such as:
- Search for weather forecast in different cities around the world.
- Current temperature.
- Maximum and minimum temperatures.
- Current description of the weather and city.
- Carousel with time, icon and temperature for the next 48 hours.
- Weather information for the next 8 days, with a dynamic temperature range bar.
- More detailed information, such as: feels like, humidity, precipitation, etc.
- Change of units, such as: metric and imperial.

### Observations
The application does not use geolocation and it is not possible to save both the city and the units. It will always start with the city being Lisbon and using the metric unit.

### Functionalities
- Search the weather forecast of any city around the world.
- Change the unit from metric to imperial, or vice versa.
- Reload weather forecast data.

### Getting Started
To run this application in development mode, you'll need to create an account on OpenWeatherMap and obtain an API Key. Once you obtain this key, you simply need to paste it into the project's "API_KEY" constant.

