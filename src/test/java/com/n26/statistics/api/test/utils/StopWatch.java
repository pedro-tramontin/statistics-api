package com.n26.statistics.api.test.utils;

public class StopWatch {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void start() {
        threadLocal.set(System.nanoTime());
    }

    public static long get() {
        return System.nanoTime() - threadLocal.get();
    }
}
