import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestConcurrencyFixedWindowRateLimiter {
    public static void main(String[] args) throws InterruptedException {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
        String user = "concurrent-user";

        final int totalThreads = 50;
        final int requestsPerThread = 50; // total requests = 2500

        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger denied = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(totalThreads);
        ExecutorService exec = Executors.newFixedThreadPool(totalThreads);

        for (int t = 0; t < totalThreads; t++) {
            exec.submit(() -> {
                for (int i = 0; i < requestsPerThread; i++) {
                    boolean ok = limiter.allowRequest(user);
                    if (ok) allowed.incrementAndGet(); else denied.incrementAndGet();
                }
                latch.countDown();
            });
        }

        latch.await();
        exec.shutdownNow();

        int total = totalThreads * requestsPerThread;
        System.out.println("--- Concurrency Test ---");
        System.out.println("Total requests: " + total);
        System.out.println("Allowed: " + allowed.get());
        System.out.println("Denied: " + denied.get());

        if (allowed.get() > 100) {
            System.out.println("NOTE: allowed > REQUEST_LIMIT (100). A race condition was observed in this run.");
        } else {
            System.out.println("No race observed (allowed <= REQUEST_LIMIT).");
        }
    }
}
