package com.chimelong.storefront.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Created on 09/05/2018.
 */
public class DateTimeHelper
{

    public static final String BCF_12HOUR_FORMAT = "h:mm a";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    /**
     * @param dateTime
     *           standard format yyyy-MM-dd'T'HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(final String dateTime)
    {
        return LocalDateTime.parse(dateTime, fmt);
    }

    /**
     * @param dateTime
     *           standard format yyyy-MM-dd'T'HH:mm:ss
     * @return LocalDate
     */
    public static LocalDate getLocalDate(final String dateTime)
    {
        return LocalDate.parse(dateTime, fmt);
    }

    /**
     * @param dateTime
     *           standard format yyyy-MM-dd'T'HH:mm:ss
     * @param targetFormat
     *           new format string
     * @return dateTime string
     */
    public static String getString(final String dateTime, final String targetFormat)
    {
        return getString(dateTime, DEFAULT_DATE_FORMAT, targetFormat);
    }

    /**
     * @param dateTime
     *           time string in your format
     * @param sourceFormat
     *           your format string
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(final String dateTime, final String sourceFormat)
    {
        final DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(sourceFormat);
        return LocalDateTime.parse(dateTime, newFormatter);
    }

    /**
     * @param dateTime
     *           time string in your format
     * @param sourceFormat
     *           your format string
     * @return LocalDate
     */
    public static LocalDate getLocalDate(final String dateTime, final String sourceFormat)
    {
        final DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(sourceFormat);
        return LocalDate.parse(dateTime, newFormatter);
    }

    /**
     * @param dateTime
     *           time string in your format
     * @param sourceFormat
     *           old format string
     * @param targetFormat
     *           new format string
     * @return dateTime string
     */
    public static String getString(final String dateTime, final String sourceFormat, final String targetFormat)
    {
        final TemporalAccessor temporalAccessor = DateTimeFormatter.ofPattern(sourceFormat).parse(dateTime);
        final DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern(targetFormat);
        if (temporalAccessor.isSupported(ChronoField.HOUR_OF_DAY))
        {
            final LocalDateTime time = LocalDateTime.from(temporalAccessor);
            return time.format(targetFormatter);
        }
        else
        {
            final LocalDate date = LocalDate.from(temporalAccessor);
            final LocalTime time = LocalTime.of(0, 0, 0, 0);
            return LocalDateTime.of(date, time).format(targetFormatter);
        }
    }

    /**
     * Convert from java.util.Date to LocalDateTime
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(final Date date)
    {
        final Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Convert from java.util.Date to LocalDate
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate getLocalDate(final Date date)
    {
        final Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convert from LocalDateTime to java.util.Date
     *
     * @param dateTime
     * @return Date
     */
    public static Date LocalDateTimeToDate(final LocalDateTime dateTime)
    {
        final Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * Convert from java.util.Date to String
     *
     * @param date
     * @param format
     * @return String
     */
    public static String dateToString(final Date date, final String format)
    {
        final LocalDateTime localDateTime = getLocalDateTime(date);
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * check data1 is before than the data2
     *
     * @param data1
     * @param data2
     * @return boolean
     */
    public static boolean isBefore(final Date data1, final Date data2)
    {
        final LocalDateTime localeDataTime1 = DateTimeHelper.getLocalDateTime(data1);
        final LocalDateTime localeDataTime2 = DateTimeHelper.getLocalDateTime(data2);

        return localeDataTime1.isBefore(localeDataTime2);
    }

    /**
     * Change dateTime string into 12-hour string
     *
     * @param dateTime     dateTime string
     * @param sourceFormat source format
     * @return String like "12:00 noon" or "1:00 pm"
     */
    public static String get12HourTime(String dateTime, final String sourceFormat) {
        return get12HourTime(getLocalDateTime(dateTime, sourceFormat));
    }

    /**
     * Change localDateTime into 12-hour string
     *
     * @param localDateTime localDateTime
     * @return String like "12:00 noon" or "1:00 pm"
     */
    public static String get12HourTime(LocalDateTime localDateTime) {
        String timeStr = localDateTime.format(DateTimeFormatter.ofPattern(BCF_12HOUR_FORMAT));
        timeStr = timeStr.toLowerCase();
        if (localDateTime.getHour() == 0 || localDateTime.getHour() == 12) {
            if (timeStr.contains("am")) {
                return timeStr.replace("am", "midnight");
            } else if (timeStr.contains("pm")) {
                return timeStr.replace("pm", "noon");
            }
        }
        return timeStr;
    }

}