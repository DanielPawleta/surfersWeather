package surfersWeather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import surfersWeather.exceptions.DateStringValidator;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DateStringValidator dateStringValidator() {
        return new DateStringValidator();
    }
}
