package surfersWeather.model;


import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum CitiesEnum {

    Bridgetown(13.10732, -59.62021),
    Fortaleza(-3.71722, -38.54306),
    Jastarnia(54.69606, 18.67873),
    LeMorne(-20.4563, 57.3082),
    Pissouri(34.66942, 32.70132);

    private final String lat;
    private final String lon;

    CitiesEnum(double lat, double lon) {
        this.lat = String.valueOf(lat);
        this.lon = String.valueOf(lon);
    }

    public static Stream<CitiesEnum> toStream() {
        return Stream.of(CitiesEnum.values());
    }
}