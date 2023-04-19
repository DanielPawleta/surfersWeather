package surfersWeather.testUtility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import surfersWeather.model.WeatherbitResponseDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class StubResponsesBuilder {

    private WeatherbitResponseDTO weatherbitResponseDTO;
    private List<WeatherbitResponseDTO> weatherbitResponseDTOListWithStubbedResponses;

    public StubResponsesBuilder() {
        this.createWeatherbitResponseDTOListWithStubbedResponses();
    }

    private void createWeatherbitResponseDTOListWithStubbedResponses() {
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
}
