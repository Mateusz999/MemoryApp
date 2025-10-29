package com.example.memoryapp;

public interface DelayExecutor {
    void postDelayed(Runnable task, long delayMillis);
}
