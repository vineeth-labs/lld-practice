public class TestFixedWindowRateLimiter {
    public static void main(String[] args) {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
        String user = "test-user";

        int totalRequests = 105;
        int allowed = 0;
        int denied = 0;

        for (int i = 1; i <= totalRequests; i++) {
            boolean ok = limiter.allowRequest(user);
            if (ok) allowed++; else denied++;
        }

        // basic assertions - run with assertions enabled (-ea)
        assert allowed == 100 : "Expected 100 allowed, got " + allowed;
        assert denied == 5 : "Expected 5 denied, got " + denied;

        System.out.println("TestFixedWindowRateLimiter: SUCCESS");
    }
}
