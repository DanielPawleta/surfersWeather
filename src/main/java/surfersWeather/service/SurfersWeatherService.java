package surfersWeather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import surfersWeather.model.CitiesEnum;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.model.WeatherbitResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SurfersWeatherService {
    private final String EXTERNAL_API_URI;
    private final RestTemplate restTemplate;
    private final WeatherbitResponseService weatherbitResponseService;

    public SurfersWeatherService(
            @Value("${EXTERNAL_API_URI}") String EXTERNAL_API_URI,
            RestTemplate restTemplate,
            WeatherbitResponseService weatherbitResponseService) {
        this.EXTERNAL_API_URI = EXTERNAL_API_URI;
        this.restTemplate = restTemplate;
        this.weatherbitResponseService = weatherbitResponseService;
    }

    public ForecastForRequestedDate getForecast(String dateString) {
        weatherbitResponseService.setWeatherbitResponseDTOList(getForecastForEnumCities());
        weatherbitResponseService.setDateString(dateString);
        return weatherbitResponseService.findBestCityForecast();
    }

    public List<WeatherbitResponseDTO> getForecastForEnumCities() {
        List<WeatherbitResponseDTO> weatherbitResponseDTOList = new ArrayList<>();

        CitiesEnum.toStream().forEach(city -> weatherbitResponseDTOList.add(getForecastFromExternalAPI(city)));
        return weatherbitResponseDTOList;
    }

    public WeatherbitResponseDTO getForecastFromExternalAPI(CitiesEnum city) {
        String lat = city.getLat();
        String lon = city.getLon();
        String uri = String.format(EXTERNAL_API_URI, lat, lon);
        WeatherbitResponseDTO weatherbitResponseDTO = restTemplate.getForObject(uri, WeatherbitResponseDTO.class);

        Optional.ofNullable(weatherbitResponseDTO).orElseGet(() -> {
            log.warn("Couldn't get data, weather response is null");
            return null;
        });
        return weatherbitResponseDTO;
    }
}
