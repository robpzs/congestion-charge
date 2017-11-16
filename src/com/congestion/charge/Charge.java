package com.congestion.charge;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author robpzs
 */
public class Charge {

    private Duration duration;
    private BigDecimal amount;

    public Charge(Duration duration, BigDecimal amount) {
        this.duration = duration;
        this.amount = amount.setScale(1, BigDecimal.ROUND_HALF_DOWN);
    }

    public static Charge ofPrice(Duration duration, BigDecimal price) {
        BigDecimal durationInHours = new BigDecimal(duration.toMinutes()).divide(new BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal amount = durationInHours.multiply(price);
        return new Charge(duration, amount);
    }

    public Duration getDuration() {
        return duration;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Charge other = (Charge) object;

        return duration.equals(other.duration) && amount.equals(other.amount);
    }

    @Override
    public String toString() {
        return String.format("[Duration (min) = %d; Amount = %,.2f]",
                duration.toMinutes(),
                amount);
    }
}
