package surfersWeather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import surfersWeather.model.WeatherbitResponseDTO;

@Slf4j
@Service
public class SurfersWeatherService {
    private static final String EXTERNAL_API_URI = "https://api.weatherbit.io/v2.0/forecast/daily?&city=jastarnia&key=9744edc6d44049b0aeb6a3e5c3e1a18e";

    public void getForecast(String dateString) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherbitResponseDTO weatherbitResponseDTO = restTemplate.getForObject(EXTERNAL_API_URI, WeatherbitResponseDTO.class);

        if (weatherbitResponseDTO!=null) {
            log.info(weatherbitResponseDTO.getCityName());
            log.info(weatherbitResponseDTO.getConditionsForDateDTOList().get(0).getTemp());
            log.info(weatherbitResponseDTO.getConditionsForDateDTOList().get(0).getWindSpeed());
        }
        else {
            log.warn("Couldn't get data");
        }
    }
}
