package surfersWeather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionsForDateDTO {

    @JsonProperty("valid_date")
    private String validDate;
    @JsonProperty("wind_spd")
    private String windSpeed;
    private String temp;
}
