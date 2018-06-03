package com.n26.statistics.api.test.utils;

/**
 * Simple StopWatch
 */
public class StopWatch {

    /**
     * Stores the data from the StopWatch in this ThreadLocal
     */
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * Starts measuring the time
     */
    public static void start() {
        threadLocal.set(System.nanoTime());
    }

    /**
     * Gets the interval between now and the time the StopWatch was started
     */
    public static long get() {
        return System.nanoTime() - threadLocal.get();
    }
}
