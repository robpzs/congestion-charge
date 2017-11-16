package com.congestion.charge;

import com.congestion.charge.vehicle.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author robpzs
 */
public class Receipt extends Rate {

    private Charge amCharge;
    private Charge pmCharge;
    private Charge totalCharge;

    public Receipt(LocalDateTime start, LocalDateTime end, Vehicle vehicle) {
        Duration durationAm = Duration.ZERO;
        Duration durationPm = Duration.ZERO;

        LocalDateTime date = start;
        while (date.isBefore(end)) {
            LocalDateTime future;

            if (isWeekend(date)) {
                future = LocalDateTime.of(date.toLocalDate().plusDays(1), AM_FROM);
            } else if (isAmRate(date)) {
                future = LocalDateTime.of(date.toLocalDate(), AM_TILL);
                durationAm = durationAm.plus(getDuration(date, future, end));
            } else if (isPmRate(date)) {
                future = LocalDateTime.of(date.toLocalDate(), PM_TILL);
                durationPm = durationPm.plus(getDuration(date, future, end));
            } else {
                // Free time
                long addDay = date.toLocalTime().getHour() < AM_FROM.getHour() ? 0L : 1L;
                future = LocalDateTime.of(date.toLocalDate().plusDays(addDay), AM_FROM);
            }

            date = future;
        }

        this.amCharge = Charge.ofPrice(durationAm, vehicle.getAmPrice());
        this.pmCharge = Charge.ofPrice(durationPm, vehicle.getPmPrice());
        this.totalCharge = new Charge(durationAm.plus(durationPm), amCharge.getAmount().add(pmCharge.getAmount()));
    }

    public Charge getAmCharge() {
        return amCharge;
    }

    public Charge getPmCharge() {
        return pmCharge;
    }

    public Charge getTotalCharge() {
        return totalCharge;
    }

    private Duration getDuration(LocalDateTime current, LocalDateTime future, LocalDateTime end) {
        Duration duration = Duration.between(current, future);
        if (future.isAfter(end)) {
            duration = duration.minus(Duration.between(end, future));
        }
        return duration;
    }

    private String getLine(Charge charge, String rateTitle) {
        Duration duration = charge.getDuration();
        return String.format("Charge for %dh %dm (%s rate): £%,.2f",
                duration.toHours(),
                duration.minusHours(duration.toHours()).toMinutes(),
                rateTitle,
                charge.getAmount());
    }

    @Override
    public String toString() {
        return String.format("%s\n\n%s\n\nTotal Charge: £%,.2f",
                getLine(amCharge, "AM"),
                getLine(pmCharge, "PM"),
                totalCharge.getAmount());
    }
}
