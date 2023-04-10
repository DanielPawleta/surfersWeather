package surfersWeather.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import surfersWeather.model.ConditionsForDateDTO;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.model.WeatherbitResponseDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Slf4j
@Service
public class ForecastCalculateService {

    private static final int MIN_TEMP = 5;
    private static final int MAX_TEMP = 35;
    private static final int MIN_WIND_SPEED = 5;
    private static final int MAX_WIND_SPEED = 18;
    private List<ForecastForRequestedDate> forecastForRequestedDateList;
    private List<WeatherbitResponseDTO> weatherbitResponseDTOList;
    private String dateString;

    public ForecastForRequestedDate findBestCityForecast() {
        createForecastForRequestedDateObjectFromResponsesMeetingRequirements();
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

    public void createForecastForRequestedDateObjectFromResponsesMeetingRequirements() {
        forecastForRequestedDateList = new ArrayList<>();
        weatherbitResponseDTOList.forEach(weatherbitResponseDTO ->
                weatherbitResponseDTO.getConditionsForDateDTOList().stream()
                        .filter(this::filterConditions)
                        .forEach(conditionsForDateDTO ->
                                createForecastForRequestedDateObject(weatherbitResponseDTO,
                                        conditionsForDateDTO)));
    }

    public boolean filterConditions(ConditionsForDateDTO conditionsForDateDTO) {
        return conditionsForDateDTO.getValidDate().equals(dateString) &&
                isBetween(Double.parseDouble(conditionsForDateDTO.getTemp()), MIN_TEMP, MAX_TEMP) &&
                isBetween(Double.parseDouble(conditionsForDateDTO.getWindSpeed()), MIN_WIND_SPEED, MAX_WIND_SPEED);
    }

    private void createForecastForRequestedDateObject(WeatherbitResponseDTO weatherbitResponseDTO, ConditionsForDateDTO conditionsForDateDTO) {
        String cityName = weatherbitResponseDTO.getCityName();
        log.info("City name = {} ", cityName);
        String temp = conditionsForDateDTO.getTemp();
        String windSpeed = conditionsForDateDTO.getWindSpeed();
        double pointsForConditions = calculatePointsForConditions(temp, windSpeed);
        ForecastForRequestedDate forecastForRequestedDate = new ForecastForRequestedDate(cityName, temp, windSpeed, pointsForConditions, "");
        forecastForRequestedDateList.add(forecastForRequestedDate);
    }

    public double calculatePointsForConditions(String temp, String windSpd) {
        log.info("Temp = {} wind speed = {}", temp, windSpd);
        double temperature = Double.parseDouble(temp);
        double windSpeed = Double.parseDouble(windSpd);
        double pointsForConditions = windSpeed * 3 + temperature;
        log.info("Points = {}", pointsForConditions);
        return pointsForConditions;
    }

    private boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }

    private Optional<ForecastForRequestedDate> getMaxCalculatedPointsForConditions() {
        return forecastForRequestedDateList.stream()
                .max(Comparator.comparing(ForecastForRequestedDate::getPointsForConditions));
    }
}