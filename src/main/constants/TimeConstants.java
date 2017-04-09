package main.constants;

import java.time.format.DateTimeFormatter;

public class TimeConstants {

    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int DAYS_IN_YEAR = 365;
    public static final DateTimeFormatter YEAR_MONTH_DAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
