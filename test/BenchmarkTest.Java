import java.util.concurrent.*;

public class BenchmarkTest {
    public static void main(String [] args) throws Exception {

        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(1_000_000, 100_000);

        int numThreads = 10;
        int requestsPerThread = 100_000;
        int totalRequests = numThreads * requestsPerThread;

        System.out.println("Configuration:");
        System.out.println("- Threads: " + numThreads);
        System.out.println("- Requests per thread " + requestsPerThread);
        System.out.println("- Total requests: " + totalRequests);
        System.out.println("\nRunning Benchmark...\n");

        long startTime = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < requestsPerThread; j++) {
                    limiter.tryConsume(1);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        
        long endTime = System.nanoTime();

        long durationNanos = endTime - startTime;
        double durationSeconds = durationNanos / 1_000_000_000.0;
        double requestsPerSecond = totalRequests / durationSeconds;
        double latencyMicros = (durationNanos / (double) totalRequests) / 1000.0;

        System.out.println("=== Results ===");
        System.out.println("Total time: " + String.format("%.2f", durationSeconds) + " seconds");
        System.out.println("Throughput: " + String.format("%.0f", requestsPerSecond) + " requests/sec");
        System.out.println("Average latency: " + String.format("%.2f", latencyMicros) + "microseconds");
        System.out.println("\n=== Summary ===");
        System.out.println("Rate limiter can handle " + String.format("%.0f", requestsPerSecond) + " req/sec");
    }
}