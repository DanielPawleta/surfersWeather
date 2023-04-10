package surfersWeather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import surfersWeather.model.CitiesEnum;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.model.WeatherbitResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherbitAPIService {

    private final RestTemplate restTemplate;
    private final ForecastCalculateService forecastCalculateService;
    @Value("${EXTERNAL_API_URI}")
    private String EXTERNAL_API_URI;

    public ForecastForRequestedDate getBestForecastForRequestedDate(String dateString) {
        forecastCalculateService.setWeatherbitResponseDTOList(getForecastForEnumCities());
        forecastCalculateService.setDateString(dateString);
        return forecastCalculateService.findBestCityForecast();
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
        if (weatherbitResponseDTO == null) {
            log.warn("Couldn't get data, weather response is null");
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return weatherbitResponseDTO;
    }
}
