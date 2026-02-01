public class TockenBucketRateLimiter {
    /* 
    Initliaze
        - tore token capacity
        - current tokens being held
        - how many tokens we add per second
    */

    private long capacity;
    private long tokens;
    private long refillRate;
    private long lastRefillTime;

    public TockenBucketRateLimiter(long capacity, long refillRate) {
        this.capacity = capacity;
        this.tokens = capacity; // start tokens full
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Try to use tockens
    // Return true if enough tokens
    // False if not
    public synchronized boolean tryConsume(int tokensNeeded) {
        refill();

        if (tokens >= tokensNeeded) {
            tokens -= tokensNeeded;
            return true;
        }

        return false;
    }

    // Add tokens based on time
    private void refill() {
        long now = System.currentTimeMillis();
        long timePassed = now - lastRefillTime;

        long tokensToAdd = (timePassed * refillRate) / 1000;

        if (tokensToAdd > 0) {
            
            // add tokens dont exceed capacity
            tokens = Math.min(tokens + tokensToAdd, capacity);
            lastRefillTime = now;
        }
    }
}
