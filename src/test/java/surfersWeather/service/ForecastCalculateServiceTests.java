package surfersWeather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.model.WeatherbitResponseDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.mockito.Mockito.doNothing;

@Slf4j
public class ForecastCalculateServiceTests {

    WeatherbitResponseDTO weatherbitResponseDTO;
    List<WeatherbitResponseDTO> weatherbitResponseDTOListWithStubbedResponses;
    List<ForecastForRequestedDate> expectedForecastForRequestedDateList;

    @BeforeEach
    public void init_WeatherbitResponseListWithStubbedResponses() {
        ObjectMapper objectMapper = new ObjectMapper();
        weatherbitResponseDTOListWithStubbedResponses = new ArrayList<>();
        try {
            Files.walk(Path.of("src/test/resources/testStubResponses")).forEach(path -> {
                        File file = new File(String.valueOf(path));
                        if (file.isFile()) {
                            try {
                                weatherbitResponseDTO = objectMapper.readValue(file, WeatherbitResponseDTO.class);
                                weatherbitResponseDTOListWithStubbedResponses.add(weatherbitResponseDTO);
                            } catch (IOException ex) {
                                log.warn("ObjectMapper couldn't read file to WeatherbitResponseDTO");
                                ex.printStackTrace();
                            }
                        }
                    }
            );
        } catch (IOException ioException) {
            log.warn("Couldn't read stubResponses files from directory");
            ioException.printStackTrace();
        }
    }

    @BeforeEach
    public void init_ExpectedForecastForRequestedDateList() {
        enum EnumExpectedCityForecast {
            Bridgetown("Bridgetown", "25.4", "9.6", 54.199999999999996),
            Fortaleza("Fortaleza", "27.6", "5.7", 44.7);

            final String cityName;
            final String temp;
            final String windSpeed;
            final double pointsForConditions;

            EnumExpectedCityForecast(String cityName, String temp, String windSpeed, double pointsForConditions) {
                this.cityName = cityName;
                this.temp = temp;
                this.windSpeed = windSpeed;
                this.pointsForConditions = pointsForConditions;
            }
        }
        expectedForecastForRequestedDateList = new ArrayList<>();
        for (EnumExpectedCityForecast enumExpectedCityForecast : EnumExpectedCityForecast.values()) {
            ForecastForRequestedDate forecastForRequestedDate = new ForecastForRequestedDate();
            forecastForRequestedDate.setCityName(enumExpectedCityForecast.cityName);
            forecastForRequestedDate.setTemp(enumExpectedCityForecast.temp);
            forecastForRequestedDate.setWindSpeed(enumExpectedCityForecast.windSpeed);
            forecastForRequestedDate.setPointsForConditions(enumExpectedCityForecast.pointsForConditions);
            expectedForecastForRequestedDateList.add(forecastForRequestedDate);
        }
        expectedForecastForRequestedDateList.sort(Comparator.comparing(ForecastForRequestedDate::getPointsForConditions));
    }

    @Test
    public void calculatePointsForConditions_should_match_expected_values() {
        enum EnumConditionsTest {
            GoodConditions("11.2", "24.5", 58.1),
            GreatConditions("18", "35", 89),
            PoorConditions("5.7", "5.9", 23);

            final String windSpeed;
            final String temp;
            final double expectedPoints;

            EnumConditionsTest(String windSpeed, String temp, double expectedPoints) {
                this.windSpeed = windSpeed;
                this.temp = temp;
                this.expectedPoints = expectedPoints;
            }
        }
        for (EnumConditionsTest enumConditionsTest : EnumConditionsTest.values()) {
            ForecastCalculateService forecastCalculateService = new ForecastCalculateService();
            double calculatedPoints = forecastCalculateService.calculatePointsForConditions(enumConditionsTest.temp, enumConditionsTest.windSpeed);
            Assertions.assertTrue((Math.abs(enumConditionsTest.expectedPoints - calculatedPoints) < 0.01), "Calculated points don't match with expected");
        }
    }

    @Test
    public void createForecastForRequestedDateObject_with_stubbed_responses_should_match_hardcoded_list() {
        ForecastCalculateService forecastCalculateService = new ForecastCalculateService();
        forecastCalculateService.setWeatherbitResponseDTOList(weatherbitResponseDTOListWithStubbedResponses);
        forecastCalculateService.setDateString("2023-02-11");
        forecastCalculateService.createForecastForRequestedDateObjectFromResponsesMeetingRequirements();

        List<ForecastForRequestedDate> forecastForRequestedDateListFromMethod = forecastCalculateService.getForecastForRequestedDateList();
        forecastForRequestedDateListFromMethod.sort(Comparator.comparing(ForecastForRequestedDate::getPointsForConditions));

        AtomicBoolean same = new AtomicBoolean(true);
        IntStream.range(0, forecastForRequestedDateListFromMethod.size())
                .forEach(i -> {
                    if (!forecastForRequestedDateListFromMethod.get(i).getCityName().equals(expectedForecastForRequestedDateList.get(i).getCityName()))
                        same.set(false);
                    if (!forecastForRequestedDateListFromMethod.get(i).getTemp().equals(expectedForecastForRequestedDateList.get(i).getTemp()))
                        same.set(false);
                    if (!forecastForRequestedDateListFromMethod.get(i).getWindSpeed().equals(expectedForecastForRequestedDateList.get(i).getWindSpeed()))
                        same.set(false);
                    if (Double.compare(forecastForRequestedDateListFromMethod.get(i).getPointsForConditions(), (expectedForecastForRequestedDateList.get(i).getPointsForConditions())) != 0)
                        same.set(false);
                });
        Assertions.assertTrue(same.get(), "Created ForecastForRequestedDate objects list don't match with expected list from stubbed responses");
    }

    @Test
    public void findBestCityForecast_from_stubbed_responses_list_should_return_given_city_name() {
        String expectedBestCityName = "Bridgetown";
        ForecastCalculateService forecastCalculateServiceSpy = Mockito.spy(ForecastCalculateService.class);
        doNothing().when(forecastCalculateServiceSpy).createForecastForRequestedDateObjectFromResponsesMeetingRequirements();
        forecastCalculateServiceSpy.setForecastForRequestedDateList(expectedForecastForRequestedDateList);
        ForecastForRequestedDate forecastForRequestedDate = forecastCalculateServiceSpy.findBestCityForecast();
        Assertions.assertEquals(expectedBestCityName, forecastForRequestedDate.getCityName(), "Calculated location don't match with expected");
    }
}