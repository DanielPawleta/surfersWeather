package surfersWeather.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DateStringParamException.class)
    public ResponseEntity<String> handleParamConstraintViolation(DateStringParamException dateStringParamException) {//nie ex tylko nazwa
        String body = dateStringParamException.getMessage();
        log.warn("DateStringValidator rejected provided param");
        log.warn(body);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
        String body = httpClientErrorException.getMessage();
        log.warn("Free use of Weatherbit Api reached limit of 50 requests per day");
        log.warn(body);
        return new ResponseEntity<>(body, HttpStatus.TOO_MANY_REQUESTS);
    }
}
