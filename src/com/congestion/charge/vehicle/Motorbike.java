package com.congestion.charge.vehicle;

import java.math.BigDecimal;

/**
 * @author robpzs
 */
public class Motorbike extends Vehicle {

    @Override
    public BigDecimal getAmPrice() {
        return new BigDecimal("1.00");
    }

    @Override
    public BigDecimal getPmPrice() {
        return getAmPrice();
    }

}
