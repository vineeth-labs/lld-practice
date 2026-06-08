public class Main {
    public static void main(String[] args) throws InterruptedException {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
        String user = "user-1";

        int totalRequests = 105;
        int allowed = 0;
        int denied = 0;

        for (int i = 1; i <= totalRequests; i++) {
            boolean ok = limiter.allowRequest(user);
            if (ok) {
                allowed++;
            } else {
                denied++;
            }
            if (i % 10 == 0 || !ok) {
                System.out.printf("Request %3d: %s\n", i, ok ? "ALLOWED" : "DENIED");
            }
        }

        System.out.println("--- Summary ---");
        System.out.println("Total requests: " + totalRequests);
        System.out.println("Allowed: " + allowed);
        System.out.println("Denied: " + denied);
    }
}
