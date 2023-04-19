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
    private static final int WIND_SPEED_FACTOR = 3;
    private static final String FOUND_GOOD_CONDITIONS_MESSAGE = "Found best conditions at: ";
    private static final String NOT_FOUND_GOOD_CONDITIONS_MESSAGE = "No good conditions found";
    private List<ForecastForRequestedDate> forecastForRequestedDateList;
    private List<WeatherbitResponseDTO> weatherbitResponseDTOList;
    private String dateString;

    public ForecastForRequestedDate findBestCityForecast() {
        createForecastForRequestedDateObjectFromResponsesMeetingRequirements();
        Optional<ForecastForRequestedDate> cityForecastOptional = getMaxCalculatedPointsForConditions();
        if (cityForecastOptional.isPresent()) {
            log.info(FOUND_GOOD_CONDITIONS_MESSAGE + cityForecastOptional.get().getCityName());
            cityForecastOptional.get().setMessage(FOUND_GOOD_CONDITIONS_MESSAGE);
            return cityForecastOptional.get();
        } else {
            log.info(NOT_FOUND_GOOD_CONDITIONS_MESSAGE);
            ForecastForRequestedDate forecastForRequestedDate = new ForecastForRequestedDate();
            forecastForRequestedDate.setMessage(NOT_FOUND_GOOD_CONDITIONS_MESSAGE);
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
        log.info("Creating forecast for requested date object for city = {} ", cityName);
        String temp = conditionsForDateDTO.getTemp();
        String windSpeed = conditionsForDateDTO.getWindSpeed();
        double pointsForConditions = calculatePointsForConditions(temp, windSpeed);
        ForecastForRequestedDate forecastForRequestedDate = new ForecastForRequestedDate(
                cityName,
                temp,
                windSpeed,
                pointsForConditions,
                "");
        forecastForRequestedDateList.add(forecastForRequestedDate);
    }

    public double calculatePointsForConditions(String temp, String windSpd) {
        log.info("Temp = {} wind speed = {}", temp, windSpd);
        double temperature = Double.parseDouble(temp);
        double windSpeed = Double.parseDouble(windSpd);
        double pointsForConditions = windSpeed * WIND_SPEED_FACTOR + temperature;
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