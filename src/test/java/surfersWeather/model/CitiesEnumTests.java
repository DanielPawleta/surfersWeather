package surfersWeather.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CitiesEnumTests {

    @Test
    public void latAndLonCheckForCitiesEnum_should_be_within_allowed_range() {
        CitiesEnum.toStream().forEach(city -> {
            boolean condition = true;
            double lat = Double.parseDouble(city.getLat());
            if (lat > 90 || lat < -90) condition = false;
            double lon = Double.parseDouble(city.getLon());
            if (lon > 180 || lon < -180) condition = false;
            Assertions.assertTrue(condition, "Wrong coordinates for " + city.name());
        });
    }
}