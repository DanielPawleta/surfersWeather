package surfersWeather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import surfersWeather.controller.Controller;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SurfersWeatherApplicationTests {

	@Autowired
	private Controller controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
