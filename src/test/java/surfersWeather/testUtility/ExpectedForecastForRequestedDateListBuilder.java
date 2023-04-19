package surfersWeather.testUtility;

import lombok.Getter;
import surfersWeather.model.ForecastForRequestedDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class ExpectedForecastForRequestedDateListBuilder {
    private List<ForecastForRequestedDate> expectedForecastForRequestedDateList;

    public ExpectedForecastForRequestedDateListBuilder() {
        this.createExpectedForecastForRequestedDateList();
    }

    private void createExpectedForecastForRequestedDateList(){
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

}
