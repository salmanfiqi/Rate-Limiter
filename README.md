# Token Bucket Rate Limiter

A thread-safe token bucket rate limiter implementation in Java.

## Overview

This project implements the token bucket algorithm for rate limiting, commonly used in distributed systems to control the rate of requests to a service.

## Features

- Thread-safe implementation using Java concurrency primitives
- Configurable bucket capacity and refill rate
- [Add other features you implement]

## Usage
```java
// Create a rate limiter: 10 tokens capacity, refills 1 token per second
TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 1);

// Try to acquire a token
if (limiter.tryAcquire()) {
    // Request allowed
} else {
    // Request denied - rate limited
}
```

## Building
```bash
mvn clean install
```

## Running Tests
```bash
mvn test
```