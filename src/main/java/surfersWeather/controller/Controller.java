package surfersWeather.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import surfersWeather.exceptions.DateStringValidator;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.service.WeatherbitAPIService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final WeatherbitAPIService weatherbitAPIService;
    private final DateStringValidator dateStringValidator;

    @GetMapping("/forecast")
    public ForecastForRequestedDate getBestForecastForRequestedDate(@RequestParam("dateString") String dateString) {
        log.info("Get forecast command with date: {}", dateString);
        dateStringValidator.isDateStringValid(dateString);
        return weatherbitAPIService.getBestForecastForRequestedDate(dateString);
    }
}
