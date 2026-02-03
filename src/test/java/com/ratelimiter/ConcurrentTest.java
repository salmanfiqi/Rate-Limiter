package com.ratelimiter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ConcurrentTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Multi-Threading Test ===\n");

        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(100, 20);

        AtomicInteger totalAllowed = new AtomicInteger(0);
        AtomicInteger totalDenied = new AtomicInteger(0);

        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        System.out.println("Launching " + numThreads + " threads...");
        System.out.println("Each thread will make 20 requests");
        System.out.println("Total requests: " + (numThreads * 20) + "\n");
        

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;

            executor.submit(() -> {
                int allowed = 0;
                int denied = 0;

                for (int j = 0; j < 20; j++) {
                    if (limiter.tryConsume(1)){
                        allowed++;
                        totalAllowed.incrementAndGet();
                   
                    } else {
                        denied++;
                        totalDenied.incrementAndGet();
                    }
                }

                System.out.println("Thread " + threadId + ": " + allowed + " allowed, " + denied + " denied");
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n=== Results ===");
        System.out.println("Total Allowed: " + totalAllowed.get());
        System.out.println("Total Denied: " + totalDenied.get());
        System.out.println("Expected Allowed: ~100");

        if (totalAllowed.get() <= 100) {
            System.out.println("\n Thread Safety VERIFIED - never exceeded capacity");
        } else {
            System.out.println("\n RACE CONDITION DENIED - allowed more than capacity");
        }
    }
}