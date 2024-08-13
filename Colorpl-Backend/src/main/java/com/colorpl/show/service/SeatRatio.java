package com.colorpl.show.service;

public class SeatRatio {

    private SeatRatio() {
        throw new AssertionError();
    }

    public static final double[][] SEAT_RATIO = {
        {},
        {1.000},
        {.2500, .7500},
        {.1111, .2778, .6111},
        {.0625, .1458, .2708, .5208}
    };
}
