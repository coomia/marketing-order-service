package com.meiye.util;

import java.text.*;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author lancec
 */
public class FormatUtil {

    private FormatUtil() {
    }

    /**
     * Format currency.
     * @param pattern The pattern
     * @param value The format value
     * @return Return formated string
     */
    public static String formatCurrency(String pattern, double value) {
        NumberFormat format = new DecimalFormat(pattern);
        return format.format(value);
    }

    /**
     * Format date.
     * @param pattern The pattern
     * @param date The date
     * @return Return the formated date
     */
    public static String formatDate(String pattern, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * Format date.
     * @param pattern The pattern
     * @param date The date
     * @param locale The locale
     * @return Return the formated date
     */
    public static String formatDate(String pattern, Date date, Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        return dateFormat.format(date);
    }

    /**
     * Format number.
     * @param pattern The pattern
     * @param number The number
     * @return Return the formated number
     */
    public static String formatNumber(String pattern, double number) {
        NumberFormat format = new DecimalFormat(pattern);
        return format.format(number);
    }

    /**
     * Parse a string to date.
     * @param pattern The parse pattern
     * @param source The string
     * @return Return Date
     * @throws ParseException
     */
    public static Date parseDate(String pattern, String source) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(source);
    }

    /**
     * Parse a string to date.
     * @param pattern The parse pattern
     * @param source The string
     * @return Return Date
     * @throws ParseException
     */
    public static Date parseDate(String pattern, String source, Locale locale) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        return simpleDateFormat.parse(source);
    }

    /**
     * Formatting Date with Short, Time with Full.
     * @param date The date
     * @param locale The locale
     * @return  Return String
     */
    public static String normalFormatDate(Date date, Locale locale) {
        DateFormat format = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.FULL,
                locale.getCountry().equals("CA")
                ? new Locale(locale.getLanguage(), "US")
                : locale);
        return format.format(date);
    }
}
