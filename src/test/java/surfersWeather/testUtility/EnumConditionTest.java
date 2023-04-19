package surfersWeather.testUtility;

import lombok.Getter;

@Getter
public enum EnumConditionTest {

    GoodConditions("11.2", "24.5", 58.1),
    GreatConditions("18", "35", 89),
    PoorConditions("5.7", "5.9", 23);

    private final String windSpeed;
    private final String temp;
    private final double expectedPoints;

    EnumConditionTest(String windSpeed, String temp, double expectedPoints) {
        this.windSpeed = windSpeed;
        this.temp = temp;
        this.expectedPoints = expectedPoints;
    }
}