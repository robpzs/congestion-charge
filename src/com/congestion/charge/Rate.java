package com.congestion.charge;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author robpzs
 */
public class Rate {

    protected static final LocalTime AM_FROM = LocalTime.of(7, 0);
    protected static final LocalTime AM_TILL = LocalTime.of(12, 0);
    protected static final LocalTime PM_FROM = LocalTime.of(12, 0);
    protected static final LocalTime PM_TILL = LocalTime.of(19, 0);

    public boolean isAmRate(LocalDateTime date) {
        int hour = date.toLocalTime().getHour();
        return hour >= AM_FROM.getHour() && hour < AM_TILL.getHour();
    }

    public boolean isPmRate(LocalDateTime date) {
        int hour = date.toLocalTime().getHour();
        return hour >= PM_FROM.getHour() && hour < PM_TILL.getHour();
    }

    public boolean isWeekend(LocalDateTime date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

}
