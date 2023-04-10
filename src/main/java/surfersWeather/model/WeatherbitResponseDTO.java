package surfersWeather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherbitResponseDTO {

    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("data")
    private List<ConditionsForDateDTO> conditionsForDateDTOList;
}