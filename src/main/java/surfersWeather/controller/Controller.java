package surfersWeather.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import surfersWeather.service.SurfersWeatherService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final SurfersWeatherService surfersWeatherService;

    @GetMapping("/forecast")
    public void getForecast(@RequestParam("dateString") String dateString) {
        log.info("Get forecast command with date: {}", dateString);
        surfersWeatherService.getForecast(dateString);
    }
}
