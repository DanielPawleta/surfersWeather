package surfersWeather.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import surfersWeather.controller.Controller;
import surfersWeather.model.WeatherbitResponseDTO;
import surfersWeather.service.SurfersWeatherService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurfersWeatherServiceTests {
	private static final String EXTERNAL_API_URI = "https://api.weatherbit.io/v2.0/forecast/daily?&city=jastarnia&key=9744edc6d44049b0aeb6a3e5c3e1a18e";

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private SurfersWeatherService surfersWeatherService;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void contextLoads() {
		assertThat(surfersWeatherService).isNotNull();
	}

	@Test
	void responseListSizeTest() {
		int expectedListSize = 7;
		WeatherbitResponseDTO weatherbitResponseDTO = testRestTemplate.getForObject(EXTERNAL_API_URI,WeatherbitResponseDTO.class);
		assertThat(weatherbitResponseDTO.getConditionsForDateDTOList().size()).isEqualTo(expectedListSize);
	}

}
