package surfersWeather.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import surfersWeather.model.WeatherbitResponseDTO;

@Slf4j
@RestController
public class Controller {
    private static final String EXTERNAL_API_URI = "https://api.weatherbit.io/v2.0/forecast/daily?&city=jastarnia&key=9744edc6d44049b0aeb6a3e5c3e1a18e";

    @GetMapping("/forecast")
    public void getForecast() {
        RestTemplate restTemplate = new RestTemplate();
        WeatherbitResponseDTO weatherbitResponseDTO = restTemplate.getForObject(EXTERNAL_API_URI, WeatherbitResponseDTO.class);

        if (weatherbitResponseDTO!=null) {
            log.info(weatherbitResponseDTO.getCityName());
            log.info(weatherbitResponseDTO.getConditionsForDateDTOList().get(0).getTemp());
            log.info(weatherbitResponseDTO.getConditionsForDateDTOList().get(0).getWindSpeed());
        }
        else throw new NullPointerException();
    }
}
