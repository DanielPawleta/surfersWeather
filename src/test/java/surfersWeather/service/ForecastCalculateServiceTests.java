package surfersWeather.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import surfersWeather.model.ForecastForRequestedDate;
import surfersWeather.model.WeatherbitResponseDTO;
import surfersWeather.testUtility.EnumConditionTest;
import surfersWeather.testUtility.ExpectedForecastForRequestedDateListBuilder;
import surfersWeather.testUtility.StubResponsesBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.mockito.Mockito.doNothing;

@Slf4j
public class ForecastCalculateServiceTests {

    private List<WeatherbitResponseDTO> weatherbitResponseDTOListWithStubbedResponses;
    private List<ForecastForRequestedDate> expectedForecastForRequestedDateList;

    @BeforeEach
    public void initializeHardcodedResponses() {
        StubResponsesBuilder stubResponsesBuilder = new StubResponsesBuilder();
        weatherbitResponseDTOListWithStubbedResponses = stubResponsesBuilder.getWeatherbitResponseDTOListWithStubbedResponses();
        ExpectedForecastForRequestedDateListBuilder expectedForecastForRequestedDateListBuilder = new ExpectedForecastForRequestedDateListBuilder();
        expectedForecastForRequestedDateList = expectedForecastForRequestedDateListBuilder.getExpectedForecastForRequestedDateList();
    }

    @ParameterizedTest
    @EnumSource(EnumConditionTest.class)
    public void calculatePointsForConditions_should_match_expected_values(EnumConditionTest enumConditionTest) {
        ForecastCalculateService forecastCalculateService = new ForecastCalculateService();
        double calculatedPoints = forecastCalculateService.calculatePointsForConditions(enumConditionTest.getTemp(), enumConditionTest.getWindSpeed());
        Assertions.assertTrue((Math.abs(enumConditionTest.getExpectedPoints() - calculatedPoints) < 0.01), "Calculated points don't match with expected");
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