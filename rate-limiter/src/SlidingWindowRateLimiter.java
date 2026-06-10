import model.WindowInfo;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlidingWindowRateLimiter implements RateLimiter {
    private static final long WINDOW_SIZE_MS = 60_000L;
    private static final int REQUEST_LIMIT = 100;
    private ConcurrentMap<String, Deque<Long>> userRequests;

    public SlidingWindowRateLimiter() {
        userRequests = new ConcurrentHashMap<>();
    }
    @Override
    public boolean allowRequest(String userId) {
        Instant now = Instant.now();
        Long currentTime = now.toEpochMilli();
        AtomicBoolean allowed = new AtomicBoolean(false);
        userRequests.compute(userId, (k, requests) -> {
            if (requests == null) {
                requests = new ArrayDeque<>();
                requests.addLast(currentTime);
                allowed.set(true);
                return requests;
            }
            Long oldest = currentTime - WINDOW_SIZE_MS;
            while (!requests.isEmpty() && requests.peekFirst() < oldest) {
                requests.pollFirst();
            }
            if (requests.size() >= REQUEST_LIMIT) {
                allowed.set(false);
                return requests;
            }
            requests.addLast(currentTime);
            allowed.set(true);
            return requests;
        });
        return allowed.get();
    }
}
