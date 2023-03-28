package surfersWeather.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.model.ConditionsForDateDTO;
import surfersWeather.model.WeatherbitResponseDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Slf4j
@Service
public class WeatherbitResponseService {
    private List<ForecastForRequestedDate> forecastForRequestedDateList = new ArrayList<>();
    private List<WeatherbitResponseDTO> weatherbitResponseDTOList;
    private String dateString;

    public ForecastForRequestedDate findBestCityForecast() {
        createCityForecastForRequestedDateObjectFromResponsesMeetingRequirements();

        Optional<ForecastForRequestedDate> cityForecastOptional = getMaxCalculatedPointsForConditions();

        if (cityForecastOptional.isPresent()) {
            log.info("Found best conditions at: {}", cityForecastOptional.get().getCityName());
            cityForecastOptional.get().setMessage("Found best conditions at: ");
            return cityForecastOptional.get();
        } else {
            log.info("No good conditions found");
            ForecastForRequestedDate forecastForRequestedDate = new ForecastForRequestedDate();
            forecastForRequestedDate.setMessage("No good conditions found");
            return forecastForRequestedDate;
        }
    }

    public void createCityForecastForRequestedDateObjectFromResponsesMeetingRequirements() {
        weatherbitResponseDTOList.forEach(weatherbitResponseDTO ->
                weatherbitResponseDTO.getConditionsForDateDTOList().stream()
                        .filter(this::filterConditions)
                        .forEach(conditionsForDateDTO -> createCityForecastForRequestedDateObject(weatherbitResponseDTO, conditionsForDateDTO)));
    }

    public boolean filterConditions(ConditionsForDateDTO conditionsForDateDTO) {
        return conditionsForDateDTO.getValidDate().equals(dateString) &&
                Double.parseDouble(conditionsForDateDTO.getTemp()) >= 5 &&
                Double.parseDouble(conditionsForDateDTO.getTemp()) <= 35 &&
                Double.parseDouble(conditionsForDateDTO.getWindSpeed()) >= 5 &&
                Double.parseDouble(conditionsForDateDTO.getWindSpeed()) <= 18;
    }

    private void createCityForecastForRequestedDateObject(WeatherbitResponseDTO weatherbitResponseDTO, ConditionsForDateDTO conditionsForDateDTO) {
        String cityName = weatherbitResponseDTO.getCityName();
        log.info("City name = {} ", cityName);
        String temp = conditionsForDateDTO.getTemp();
        String windSpeed = conditionsForDateDTO.getWindSpeed();
        double pointsForConditions = calculatePointsForConditions(temp, windSpeed);

        ForecastForRequestedDate forecastForRequestedDate = new ForecastForRequestedDate(cityName, temp, windSpeed, pointsForConditions, "");
        forecastForRequestedDateList.add(forecastForRequestedDate);
    }

    public double calculatePointsForConditions(String temp, String wind_spd) {
        log.info("Temp = {} wind speed = {}", temp, wind_spd);

        double temperature = Double.parseDouble(temp);
        double windSpeed = Double.parseDouble(wind_spd);

        double pointsForConditions = windSpeed * 3 + temperature;
        log.info("Points = {}", pointsForConditions);
        return pointsForConditions;
    }

    private Optional<ForecastForRequestedDate> getMaxCalculatedPointsForConditions() {
        return forecastForRequestedDateList.stream()
                .max(Comparator.comparing(ForecastForRequestedDate::getPointsForConditions));
    }
}