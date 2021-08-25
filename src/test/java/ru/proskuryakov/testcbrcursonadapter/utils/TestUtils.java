package ru.proskuryakov.testcbrcursonadapter.utils;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestUtils {

    public static String dateToString(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return dateToString(calendar);
    }

    public static String dateToString(GregorianCalendar calendar) {
        return calendar.toZonedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
