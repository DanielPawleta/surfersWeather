package surfersWeather.exceptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateStringValidator {

    private final String regex = "\\d{4}-\\d{2}-\\d{2}";
    private final String datePattern = "yyyy-MM-dd";

    public void isDateStringValid(String dateString) {
        if (dateString == null) {
            throw new DateStringParamException("Provided date string is empty");
        }
        if (!dateString.matches(regex)) {
            throw new DateStringParamException("Wrong date pattern");
        }
        Date requestedDate;
        try {
            requestedDate = parseDateStringToDate(dateString);
        } catch (ParseException e) {
            throw new DateStringParamException("Wrong date format");
        }
        checkRequestedDateForAllowedRange(requestedDate);
    }

    public Date parseDateStringToDate(String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(dateString);
    }

    public void checkRequestedDateForAllowedRange(Date requestedDate) {
        if (ChronoUnit.DAYS.between(new Date().toInstant(), requestedDate.toInstant()) >= 6) {
            throw new DateStringParamException("Requested date beyond 7-days forecast");
        }
        if (ChronoUnit.DAYS.between(new Date().toInstant(), requestedDate.toInstant()) < 0) {
            throw new DateStringParamException("Requested date before today");
        }
    }
}
