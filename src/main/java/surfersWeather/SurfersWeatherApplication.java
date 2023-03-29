package surfersWeather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import surfersWeather.exceptions.DateStringValidator;

@SpringBootApplication
public class SurfersWeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurfersWeatherApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DateStringValidator dateStringValidator() {
        return new DateStringValidator();
    }

}
