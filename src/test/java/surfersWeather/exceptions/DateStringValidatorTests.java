package surfersWeather.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateStringValidatorTests {

    private final DateStringValidator dateStringValidator = new DateStringValidator();
    private Calendar calendar;

    @BeforeEach
    public void initializeCalendarInstance(){
        calendar = Calendar.getInstance();
    }

    @Test
    public void dateStringValidator_with_unparseable_string_should_throw_exception() {
        assertThrows(ParseException.class, () -> dateStringValidator.parseDateStringToDate("Some text"), "Parser parsed unparseable text");
    }

    @Test
    public void dateStringValidator_with_wrong_date_pattern_string_should_throw_exception() {
        assertThrows(ParseException.class, () -> dateStringValidator.parseDateStringToDate("01-01-2023"), "Parser parsed wrong pattern string");
    }

    @Test
    public void dateStringValidator_with_yesterdays_date_should_throw_exception() {
        calendar.add(Calendar.DATE, -1);
        assertThrows(DateStringParamException.class, () -> dateStringValidator.checkRequestedDateForAllowedRange(calendar.getTime()), "DateStringValidator rejected today's date");
    }

    @Test
    public void dateStringValidator_with_date_beyond_range_should_throw_exception() {
        calendar.add(Calendar.DATE, 7);
        assertThrows(DateStringParamException.class, () -> dateStringValidator.checkRequestedDateForAllowedRange(calendar.getTime()), "DateStringValidator accepted date beyond allowed range");
    }

    @Test
    public void dateStringValidator_with_todays_date_should_be_valid() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = simpleDateFormat.format(calendar.getTime());
        assertDoesNotThrow(() -> dateStringValidator.isDateStringValid(todayString), "DateStringValidator rejected today's date");
    }

    @Test
    public void dateStringValidator_with_date_within_range_should_be_valid() {
        assertDoesNotThrow(() -> dateStringValidator.checkRequestedDateForAllowedRange(calendar.getTime()), "DateStringValidator rejected correct date");
    }
}