package com.ratelimiter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Test ===\n");
        
        // Create a rate limiter:
        // - Capacity: 10 tokens
        // - Refill: 2 tokens per second
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 2);
        
        // Test 1: Make 15 requests really fast
        System.out.println("Test 1: Making 15 rapid requests");
        System.out.println("(Should allow first 10, deny last 5)\n");
        
        for (int i = 1; i <= 15; i++) {
            boolean allowed = limiter.tryConsume(1);
            
            if (allowed) {
                System.out.println("Request " + i + ": ✓ ALLOWED");
            } else {
                System.out.println("Request " + i + ": ✗ DENIED");
            }
        }
        
        // Wait 3 seconds to let tokens refill
        System.out.println("\n Waiting 3 seconds for tokens to refill...");
        System.out.println("(Should refill 2 tokens/sec by 3 sec = 6 tokens)\n");
        Thread.sleep(3000);
        
        // Test 2: Make 8 more requests
        System.out.println("Test 2: Making 8 requests after waiting");
        System.out.println("(Should allow first 6, deny last 2)\n");
        
        for (int i = 1; i <= 8; i++) {
            boolean allowed = limiter.tryConsume(1);
            
            if (allowed) {
                System.out.println("Request " + i + ": ✓ ALLOWED");
            } else {
                System.out.println("Request " + i + ": ✗ DENIED");
            }
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}