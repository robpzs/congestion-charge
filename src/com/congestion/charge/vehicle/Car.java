package com.congestion.charge.vehicle;

import java.math.BigDecimal;

/**
 * @author robpzs
 */
public class Car extends Vehicle {

    @Override
    public BigDecimal getAmPrice() {
        return new BigDecimal("2.00");
    }

    @Override
    public BigDecimal getPmPrice() {
        return new BigDecimal("2.50");
    }

}
