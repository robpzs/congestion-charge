package com.congestion.charge;

import com.congestion.charge.vehicle.Car;
import com.congestion.charge.vehicle.Motorbike;
import com.congestion.charge.vehicle.Van;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ReceiptTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Test
    public void testAmRate() {
        LocalDateTime expectedTrue1 = LocalDateTime.of(2017, 11, 16, 7, 0);
        LocalDateTime expectedTrue2 = LocalDateTime.of(2017, 11, 16, 9, 59);
        LocalDateTime expectedTrue3 = LocalDateTime.of(2017, 11, 16, 11, 59);
        LocalDateTime expectedFalse1 = LocalDateTime.of(2017, 11, 16, 6, 11);
        LocalDateTime expectedFalse2 = LocalDateTime.of(2017, 11, 16, 14, 23);
        LocalDateTime expectedFalse3 = LocalDateTime.of(2017, 11, 16, 0, 0);

        Rate rate = new Rate();

        assertTrue(rate.isAmRate(expectedTrue1));
        assertTrue(rate.isAmRate(expectedTrue2));
        assertTrue(rate.isAmRate(expectedTrue3));
        assertFalse(rate.isAmRate(expectedFalse1));
        assertFalse(rate.isAmRate(expectedFalse2));
        assertFalse(rate.isAmRate(expectedFalse3));
    }

    @Test
    public void testPmRate() {
        LocalDateTime expectedTrue1 = LocalDateTime.of(2017, 11, 16, 12, 0);
        LocalDateTime expectedTrue2 = LocalDateTime.of(2017, 11, 16, 15, 20);
        LocalDateTime expectedTrue3 = LocalDateTime.of(2017, 11, 16, 18, 59);
        LocalDateTime expectedFalse1 = LocalDateTime.of(2017, 11, 16, 6, 11);
        LocalDateTime expectedFalse2 = LocalDateTime.of(2017, 11, 16, 11, 23);
        LocalDateTime expectedFalse3 = LocalDateTime.of(2017, 11, 16, 0, 0);

        Rate rate = new Rate();

        assertTrue(rate.isPmRate(expectedTrue1));
        assertTrue(rate.isPmRate(expectedTrue2));
        assertTrue(rate.isPmRate(expectedTrue3));
        assertFalse(rate.isPmRate(expectedFalse1));
        assertFalse(rate.isPmRate(expectedFalse2));
        assertFalse(rate.isPmRate(expectedFalse3));
    }

    @Test
    public void testWeekend() {
        LocalDateTime expectedTrue1 = LocalDateTime.of(2017, 11, 11, 12, 0);
        LocalDateTime expectedTrue2 = LocalDateTime.of(2017, 11, 19, 0, 0);
        LocalDateTime expectedFalse1 = LocalDateTime.of(2017, 11, 16, 6, 11);
        LocalDateTime expectedFalse2 = LocalDateTime.of(2017, 11, 1, 11, 11);

        Rate rate = new Rate();

        assertTrue(rate.isWeekend(expectedTrue1));
        assertTrue(rate.isWeekend(expectedTrue2));
        assertFalse(rate.isWeekend(expectedFalse1));
        assertFalse(rate.isWeekend(expectedFalse2));
    }

    @Test
    public void testCharge() {
        Charge expected1 = new Charge(Duration.ofHours(2), new BigDecimal("5"));
        Charge expected2 = new Charge(Duration.ofMinutes(10), new BigDecimal("0.4"));
        Charge expected3 = new Charge(Duration.ofDays(1), new BigDecimal("60"));
        Charge expected4Wrong = new Charge(Duration.ofMinutes(60), new BigDecimal("3"));

        Charge actual1 = Charge.ofPrice(Duration.ofHours(2), new BigDecimal("2.5"));
        Charge actual2 = Charge.ofPrice(Duration.ofMinutes(10), new BigDecimal("2.5"));
        Charge actual3 = Charge.ofPrice(Duration.ofDays(1), new BigDecimal("2.5"));
        Charge actual4 = Charge.ofPrice(Duration.ofMinutes(60), new BigDecimal("2.5"));

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
        assertNotEquals(expected4Wrong, actual4);
    }

    /**
     * Car: 24/04/2008 11:32 - 24/04/2008 14:42
     */
    @Test
    public void testCarExample() {
        Charge expectedAm = new Charge(Duration.ofMinutes(28), new BigDecimal("0.90"));
        Charge expectedPm = new Charge(Duration.ofMinutes((2 * 60) + 42), new BigDecimal("6.70"));
        Charge expectedTotal = new Charge(expectedAm.getDuration().plus(expectedPm.getDuration()), new BigDecimal("7.60"));

        LocalDateTime start = LocalDateTime.parse("24/04/2008 11:32", FORMATTER);
        LocalDateTime end = LocalDateTime.parse("24/04/2008 14:42", FORMATTER);
        Receipt receipt = new Receipt(start, end, new Car());
        System.out.println("----- Car\n" + receipt + "\n-----");

        assertEquals(expectedAm, receipt.getAmCharge());
        assertEquals(expectedPm, receipt.getPmCharge());
        assertEquals(expectedTotal, receipt.getTotalCharge());
    }

    /**
     * Motorbike: 24/04/2008 17:00 - 24/04/2008 22:11
     */
    @Test
    public void testMotorbikeExample() {
        Charge expectedAm = new Charge(Duration.ZERO, new BigDecimal("0.00"));
        Charge expectedPm = new Charge(Duration.ofMinutes(2 * 60), new BigDecimal("2.00"));
        Charge expectedTotal = new Charge(expectedAm.getDuration().plus(expectedPm.getDuration()), new BigDecimal("2.00"));

        LocalDateTime start = LocalDateTime.parse("24/04/2008 17:00", FORMATTER);
        LocalDateTime end = LocalDateTime.parse("24/04/2008 22:11", FORMATTER);
        Receipt receipt = new Receipt(start, end, new Motorbike());
        System.out.println("----- Motorbike\n" + receipt + "\n-----");

        assertEquals(expectedAm, receipt.getAmCharge());
        assertEquals(expectedPm, receipt.getPmCharge());
        assertEquals(expectedTotal, receipt.getTotalCharge());
    }

    /**
     * Van: 25/04/2008 10:23 - 28/04/2008 09:02
     */
    @Test
    public void testVanExample() {
        Charge expectedAm = new Charge(Duration.ofMinutes((3 * 60) + 39), new BigDecimal("7.30"));
        Charge expectedPm = new Charge(Duration.ofMinutes(7 * 60), new BigDecimal("17.50"));
        Charge expectedTotal = new Charge(expectedAm.getDuration().plus(expectedPm.getDuration()), new BigDecimal("24.80"));

        LocalDateTime start = LocalDateTime.parse("25/04/2008 10:23", FORMATTER);
        LocalDateTime end = LocalDateTime.parse("28/04/2008 09:02", FORMATTER);
        Receipt receipt = new Receipt(start, end, new Van());
        System.out.println("----- Input 3\n" + receipt + "\n-----");

        assertEquals(expectedAm, receipt.getAmCharge());
        assertEquals(expectedPm, receipt.getPmCharge());
        assertEquals(expectedTotal, receipt.getTotalCharge());
    }

    /**
     * All day Car parking.
     */
    @Test
    public void testAllDayParking() {
        Charge expectedAm = new Charge(Duration.ofMinutes(5 * 60), new BigDecimal("10"));
        Charge expectedPm = new Charge(Duration.ofMinutes(7 * 60), new BigDecimal("17.5"));
        Charge expectedTotal = new Charge(expectedAm.getDuration().plus(expectedPm.getDuration()), new BigDecimal("27.50"));

        LocalDateTime start = LocalDateTime.parse("16/11/2017 07:00", FORMATTER);
        LocalDateTime end = LocalDateTime.parse("16/11/2017 19:00", FORMATTER);
        Receipt receipt = new Receipt(start, end, new Car());
        System.out.println("----- All day\n" + receipt + "\n-----");

        assertEquals(expectedAm, receipt.getAmCharge());
        assertEquals(expectedPm, receipt.getPmCharge());
        assertEquals(expectedTotal, receipt.getTotalCharge());
    }

}
