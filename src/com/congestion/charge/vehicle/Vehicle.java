package com.congestion.charge.vehicle;

import java.math.BigDecimal;

/**
 * @author robpzs
 */
public abstract class Vehicle {

    /**
     * Morning rate between between 7am and 12pm.
     * @return Charge rate.
     */
    public abstract BigDecimal getAmPrice();

    /**
     * Evening rate between 12pm and 7pm.
     * @return Charge rate.
     */
    public abstract BigDecimal getPmPrice();

}
