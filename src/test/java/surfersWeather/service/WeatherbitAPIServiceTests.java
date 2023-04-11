package surfersWeather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import surfersWeather.model.CitiesEnum;
import surfersWeather.model.WeatherbitResponseDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class WeatherbitAPIServiceTests {

    String fakeURI = "fake uri";
    WeatherbitResponseDTO weatherbitResponseDTO;
    List<WeatherbitResponseDTO> weatherbitResponseDTOList;

    @Mock
    RestTemplate restTemplate;

    @Spy
    ForecastCalculateService forecastCalculateService;

    @BeforeEach
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        weatherbitResponseDTOList = new ArrayList<>();
        try {
            Files.walk(Path.of("src/test/resources/testStubResponses")).forEach(path -> {
                        File file = new File(String.valueOf(path));
                        if (file.isFile()) {
                            try {
                                weatherbitResponseDTO = objectMapper.readValue(file, WeatherbitResponseDTO.class);
                                weatherbitResponseDTOList.add(weatherbitResponseDTO);
                            } catch (IOException ex) {
                                log.warn("ObjectMapper couldn't read file to WeatherbitResponseDTO");
                                ex.printStackTrace();
                            }
                        }
                    }
            );
        } catch (IOException ioException) {
            log.warn("Couldn't read testStubResponses files from directory");
            ioException.printStackTrace();
        }
    }

    @Test
    public void getForecastFromExternalAPI_should_return_expected_list_size() {
        WeatherbitAPIService mockWeatherbitAPIService = Mockito.mock(WeatherbitAPIService.class, Mockito.withSettings().useConstructor(restTemplate, forecastCalculateService));
        when(mockWeatherbitAPIService.prepareURI(Mockito.any(CitiesEnum.class))).thenReturn(fakeURI);
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(weatherbitResponseDTOList.get(0));
        when(mockWeatherbitAPIService.getForecastFromExternalAPI(Mockito.any(CitiesEnum.class))).thenCallRealMethod();
        int expectedListSize = 7;
        WeatherbitResponseDTO weatherbitResponseDTO = mockWeatherbitAPIService.getForecastFromExternalAPI(CitiesEnum.Bridgetown);
        assertThat(weatherbitResponseDTO.getConditionsForDateDTOList().size()).withFailMessage("GetForecastFromExternalAPI method didn't return expected list size from stub response").isEqualTo(expectedListSize);
    }

    @Test
    public void getForecast_should_return_expected_best_forecast() {
        WeatherbitAPIService mockWeatherbitAPIService = Mockito.mock(WeatherbitAPIService.class, Mockito.withSettings().useConstructor(restTemplate, forecastCalculateService));
        when(mockWeatherbitAPIService.getForecastForEnumCities()).thenReturn(weatherbitResponseDTOList);
        when(mockWeatherbitAPIService.getBestForecastForRequestedDate(Mockito.anyString())).thenCallRealMethod();
        String expectedBestForecastCity = "Bridgetown";
        Assertions.assertEquals(expectedBestForecastCity, mockWeatherbitAPIService.getBestForecastForRequestedDate("2023-02-11").getCityName(), "Returned best city forecast don't match with expected");
    }
}