package surfersWeather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForecastForRequestedDate {

    private String cityName;
    private String temp;
    private String windSpeed;
    private double pointsForConditions;
    private String message;
}
