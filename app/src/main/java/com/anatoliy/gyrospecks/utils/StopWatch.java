package com.anatoliy.gyrospecks.utils;

/**
 * Date: 15.06.2017
 * Time: 0:23
 *
 * @author Anatoliy
 */

public class StopWatch {
    private final static int SECONDS_IN_HOUR = 3600;
    private final static int SECONDS_IN_MINUTE = 60;
    private final static String STOPWATCH_STRING = "%s:%s";
    private final static String STOPWATCH_IS_FULL = "";


    public static String formatTime(final long seconds) {
        if (seconds < SECONDS_IN_MINUTE) {
            return String.format(STOPWATCH_STRING, formatNumbers(0), formatNumbers(seconds));
        }
        else if (seconds >= SECONDS_IN_MINUTE && seconds < SECONDS_IN_HOUR) {
            final long minutes = seconds / SECONDS_IN_MINUTE;
            final long secondsWithoutMinutes = seconds % SECONDS_IN_MINUTE;

            return String.format(STOPWATCH_STRING, formatNumbers(minutes), formatNumbers(secondsWithoutMinutes));
        } else {
            return STOPWATCH_IS_FULL;
        }
    }

    private static String formatNumbers(final long number) {
        final String valueOf = String.valueOf(number);
        if (valueOf.length() < 2) {
            return "0" + valueOf;
        } else {
            return valueOf;
        }
    }
}
