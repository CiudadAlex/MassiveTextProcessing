package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * TimeUtils.
 *
 * @author Alejandro
 *
 */
public final class TimeUtils {

    /** SECOND. */
    public static final long SECOND = 1000;

    /** MINUTE. */
    public static final long MINUTE = 60 * SECOND;

    /** HOUR. */
    public static final long HOUR = 60 * MINUTE;

    /** DAY. */
    public static final long DAY = 24 * HOUR;

    /** WEEK. */
    public static final long WEEK = 7 * DAY;

    /** MONTH. */
    public static final long MONTH = 30 * DAY;

    /** YEAR. */
    public static final long YEAR = 365 * DAY;

    /** DATE_FORMATS_PRECISSION_ASC. */
    public static final String[] DATE_FORMATS_PRECISSION_ASC = new String[] { "yyyy", "MM-yyyy", "dd-MM-yyyy",
            "HH'h' dd-MM-yyyy", "HH:mm dd-MM-yyyy", "HH:mm:ss dd-MM-yyyy", "HH:mm:ss.SSS dd-MM-yyyy" };

    private TimeUtils() {
    }

    /**
     * Creates a String with the time lapse.
     *
     * @param begin
     *            begin
     * @param end
     *            end
     * @return a String with the time lapse
     */
    public static String toStringTimeLapse(final Date begin, final Date end) {

        final long diff = end.getTime() - begin.getTime();

        final long millisUnit = diff % 1000;
        final long seconds = diff / 1000;

        final long secondsUnit = seconds % 60;
        final long minutes = seconds / 60;

        final long minutesUnit = minutes % 60;
        final long hours = minutes / 60;

        return hours + "h " + minutesUnit + "m " + secondsUnit + "s " + millisUnit + "ms";
    }

    /**
     * returns if the time range is greater than the millis given.
     *
     * @param begin
     *            begin
     * @param end
     *            end
     * @param millis
     *            millis
     * @return if the time range is greater than the millis give
     */
    public static boolean isTimeRangeGreaterThanMillisGiven(final Date begin, final Date end, final long millis) {

        final long diff = end.getTime() - begin.getTime();

        return diff > millis;
    }

    /**
     * returns if the time range is greater than the millis given.
     *
     * @param begin
     *            begin
     * @param end
     *            end
     * @param millis
     *            millis
     * @return if the time range is greater than the millis give
     */
    public static boolean isAbsoluteTimeRangeGreaterThanMillisGiven(final Date begin, final Date end,
            final long millis) {

        final long absDiff = Math.abs(end.getTime() - begin.getTime());

        return absDiff > millis;
    }

    /**
     * Sleeps the given millis.
     *
     * @param millis
     *            millis
     */
    public static void sleep(final long millis) {

        try {

            Thread.sleep(millis);

        } catch (final InterruptedException e) {
            AppLogger.logError("Error while sleeping", e);
        }
    }

    /**
     * Parses the date.
     *
     * @param strDate
     *            strDate
     * @param format
     *            format
     * @return Date
     * @throws ParseException
     */
    public static Date parseDate(final String strDate, final String format) throws ParseException {

        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(strDate);
    }

    /**
     * Parses the date.
     *
     * @param strDate
     *            strDate
     * @param format
     *            format
     * @param locale
     *            locale
     * @return Date
     * @throws ParseException
     */
    public static Date parseDate(final String strDate, final String format, final Locale locale) throws ParseException {

        final SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.parse(strDate);
    }

    /**
     * Parses the date and if it cannot parse returns null.
     *
     * @param strDate
     *            strDate
     * @param format
     *            format
     * @return Date
     * @throws ParseException
     */
    public static Date parseDateDefaultNull(final String strDate, final String format) {

        try {
            return parseDate(strDate, format);

        } catch (final ParseException e) {
            AppLogger.logError("Error parsing a date", e);
        }

        return null;
    }

    /**
     * Formats the date.
     *
     * @param date
     *            date
     * @param format
     *            format
     * @return String
     */
    public static String formatDate(final Date date, final String format) {

        if (date == null) {
            return "-";
        }

        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Creates a date.
     *
     * @param year
     *            year
     * @param month
     *            month
     * @param day
     *            day
     * @return the created date
     */
    public static Date createDate(final int year, final int month, final int day) {

        final Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Resets the calendar fields below the given calendar field.
     *
     * @param date
     *            date
     * @param calendarFieldId
     *            calendarFieldId
     * @return reseted Date
     */
    public static Date resetBelowCalendarField(final Date date, final int calendarFieldId) {

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        if (Calendar.MILLISECOND == calendarFieldId) {
            return calendar.getTime();
        }

        calendar.set(Calendar.MILLISECOND, 0);

        if (Calendar.SECOND == calendarFieldId) {
            return calendar.getTime();
        }

        calendar.set(Calendar.SECOND, 0);

        if (Calendar.MINUTE == calendarFieldId) {
            return calendar.getTime();
        }

        calendar.set(Calendar.MINUTE, 0);

        if (intInArray(calendarFieldId, Calendar.HOUR_OF_DAY, Calendar.HOUR)) {
            return calendar.getTime();
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);

        if (intInArray(calendarFieldId, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH,
                Calendar.DAY_OF_YEAR)) {
            return calendar.getTime();
        }

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        if (Calendar.MONTH == calendarFieldId) {
            return calendar.getTime();
        }

        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        if (Calendar.YEAR == calendarFieldId) {
            return calendar.getTime();
        }

        return date;
    }

    /**
     * Sets the init of the day.
     *
     * @param date
     *            date
     * @return init of the day
     */
    public static Date setInitOfDay(final Date date) {
        return resetBelowCalendarField(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * Sets the end of the day.
     *
     * @param date
     *            date
     * @return end of the day
     */
    public static Date setEndOfDay(final Date date) {

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);

        return calendar.getTime();
    }

    private static boolean intInArray(final int i, final int... arrayInt) {

        for (final int intItem : arrayInt) {
            if (i == intItem) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds the amount of calendarFieldId given.
     *
     * @param date
     *            date
     * @param calendarFieldId
     *            calendarFieldId
     * @param amount
     *            amount
     * @return the resulting date
     */
    public static Date addCalendarField(final Date date, final int calendarFieldId, final int amount) {

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendarFieldId, amount);
        return calendar.getTime();
    }

    /**
     * Sets the value of calendarFieldId given.
     *
     * @param date
     *            date
     * @param calendarFieldId
     *            calendarFieldId
     * @param value
     *            value
     * @return the resulting date
     */
    public static Date setCalendarField(final Date date, final int calendarFieldId, final int value) {

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(calendarFieldId, value);
        return calendar.getTime();
    }

    /**
     * Returns the calendar field value.
     *
     * @param date
     *            date
     * @param calendarFieldId
     *            calendarFieldId
     * @return the calendar field value
     */
    public static Integer getCalendarField(final Date date, final int calendarFieldId) {

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(calendarFieldId);
    }

    /**
     * Returns if it is the same date.
     *
     * @param date1
     *            date1
     * @param date2
     *            date2
     * @return if it is the same date
     */
    public static boolean isSameDate(final Date date1, final Date date2) {

        final Calendar cal1 = createCalendar(date1);
        final Calendar cal2 = createCalendar(date2);

        final boolean sameYear = ifFieldSameValue(cal1, cal2, Calendar.YEAR);
        final boolean sameMonth = ifFieldSameValue(cal1, cal2, Calendar.MONTH);
        final boolean sameDayOfMonth = ifFieldSameValue(cal1, cal2, Calendar.DAY_OF_MONTH);

        return sameYear && sameMonth && sameDayOfMonth;
    }

    /**
     * Returns if it is the same month.
     *
     * @param date1
     *            date1
     * @param date2
     *            date2
     * @return if it is the same month
     */
    public static boolean isSameMonth(final Date date1, final Date date2) {

        final Calendar cal1 = createCalendar(date1);
        final Calendar cal2 = createCalendar(date2);

        final boolean sameYear = ifFieldSameValue(cal1, cal2, Calendar.YEAR);
        final boolean sameMonth = ifFieldSameValue(cal1, cal2, Calendar.MONTH);

        return sameYear && sameMonth;
    }

    /**
     * Returns if it is the same year.
     *
     * @param date1
     *            date1
     * @param date2
     *            date2
     * @return if it is the same year
     */
    public static boolean isSameYear(final Date date1, final Date date2) {

        final Calendar cal1 = createCalendar(date1);
        final Calendar cal2 = createCalendar(date2);

        final boolean sameYear = ifFieldSameValue(cal1, cal2, Calendar.YEAR);

        return sameYear;
    }

    private static Calendar createCalendar(final Date date) {

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    private static boolean ifFieldSameValue(final Calendar cal1, final Calendar cal2, final int calendarFieldId) {

        return cal1.get(calendarFieldId) == cal2.get(calendarFieldId);
    }

    /**
     * Returns the Max date contained in the objects.
     *
     * @param <T>
     *            object type
     * @param listObjects
     *            listObjects
     * @param extractDate
     *            extractDate
     * @return the Max date contained in the objects
     */
    public static <T> Date getMaxDate(final List<T> listObjects, final Function<T, Date> extractDate) {

        final DateAndObject<T> dateAndObject = getMaxDateAndObject(listObjects, extractDate);

        if (dateAndObject == null) {
            return null;
        }

        return dateAndObject.getDate();
    }

    /**
     * Returns the Max date and object contained in the objects.
     *
     * @param <T>
     *            object type
     * @param listObjects
     *            listObjects
     * @param extractDate
     *            extractDate
     * @return the Max date contained in the objects
     */
    public static <T> DateAndObject<T> getMaxDateAndObject(final List<T> listObjects,
            final Function<T, Date> extractDate) {

        if (listObjects.isEmpty()) {
            return null;
        }

        long longDateMax = Long.MIN_VALUE;
        T objectMax = null;

        for (final T obj : listObjects) {

            final long longDate = extractDate.apply(obj).getTime();

            if (longDate > longDateMax) {
                longDateMax = longDate;
                objectMax = obj;
            }
        }

        return new DateAndObject<T>(new Date(longDateMax), objectMax);
    }

    /**
     * Returns the Min date contained in the objects.
     *
     * @param <T>
     *            object type
     * @param listObjects
     *            listObjects
     * @param extractDate
     *            extractDate
     * @return the Min date contained in the objects
     */
    public static <T> Date getMinDate(final List<T> listObjects, final Function<T, Date> extractDate) {

        final DateAndObject<T> dateAndObject = getMinDateAndObject(listObjects, extractDate);

        if (dateAndObject == null) {
            return null;
        }

        return dateAndObject.getDate();
    }

    /**
     * Returns the Min date and object contained in the objects.
     *
     * @param <T>
     *            object type
     * @param listObjects
     *            listObjects
     * @param extractDate
     *            extractDate
     * @return the Min date contained in the objects
     */
    public static <T> DateAndObject<T> getMinDateAndObject(final List<T> listObjects,
            final Function<T, Date> extractDate) {

        if (listObjects.isEmpty()) {
            return null;
        }

        long longDateMin = Long.MAX_VALUE;
        T objectMin = null;

        for (final T obj : listObjects) {

            final long longDate = extractDate.apply(obj).getTime();

            if (longDate < longDateMin) {
                longDateMin = longDate;
                objectMin = obj;
            }
        }

        return new DateAndObject<T>(new Date(longDateMin), objectMin);
    }

    /**
     * Returns the closest object in time.
     *
     * @param <T>
     *            object type
     * @param date
     *            date
     * @param listObjects
     *            listObjects
     * @param extractDate
     *            extractDate
     * @return the closest object in time
     */
    public static <T> T getClosestObjectInTime(final Date date, final List<T> listObjects,
            final Function<T, Date> extractDate) {

        if (listObjects.isEmpty()) {
            return null;
        }

        long diffClosest = Long.MAX_VALUE;
        T objectClosest = null;

        for (final T obj : listObjects) {

            final long longDate = extractDate.apply(obj).getTime();

            final long diffIter = Math.abs(date.getTime() - longDate);

            if (diffIter < diffClosest) {
                diffClosest = diffIter;
                objectClosest = obj;
            }
        }

        return objectClosest;
    }

    /**
     * Returns the first format that makes different the two dates.
     *
     * @param date1
     *            date1
     * @param date2
     *            date2
     * @return the first format that makes different the two dates
     */
    public static String getFistFormatThatMakesDatesDifferent(final Date date1, final Date date2) {

        for (final String format : DATE_FORMATS_PRECISSION_ASC) {

            final String strDate1 = formatDate(date1, format);
            final String strDate2 = formatDate(date2, format);

            if (!strDate1.equals(strDate2)) {
                return format;
            }
        }

        return null;
    }

    /**
     * Returns if the date is between the other two.
     *
     * @param date
     *            date
     * @param dateLow
     *            dateLow
     * @param dateHigh
     *            dateHigh
     * @return if the date is between the other two
     */
    public static boolean isBetween(final Date date, final Date dateLow, final Date dateHigh) {
        return date.getTime() >= dateLow.getTime() && date.getTime() <= dateHigh.getTime();
    }

}
