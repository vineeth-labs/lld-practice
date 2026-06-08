import model.WindowInfo;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedWindowRateLimiter implements RateLimiter {
    private static final long DEFAULT_WINDOW_SIZE = 60L;
    private static final int REQUEST_LIMIT = 100;
    private final Map<String, WindowInfo> userWindowInfo;

    public FixedWindowRateLimiter() {
        this.userWindowInfo = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allowRequest(String userId) {
        Instant timeStamp = Instant.now();
        long windowId = computeWindowId(timeStamp);
        AtomicBoolean allowed = new AtomicBoolean(false);

        userWindowInfo.compute(userId, (k, existing) -> {
            if (existing == null || existing.getWindowId() != windowId) {
                // no entry yet or window rolled over - create new window with one request
                allowed.set(true);
                return new WindowInfo(windowId, 1);
            }

            int current = existing.getRequestCount();
            if (current >= REQUEST_LIMIT) {
                // reached limit - keep existing
                allowed.set(false);
                return existing;
            }

            // increment and allow
            existing.incrementRequestCount();
            allowed.set(true);
            return existing;
        });

        return allowed.get();
    }

    private long computeWindowId(Instant timestamp) {
        return timestamp.getEpochSecond() / DEFAULT_WINDOW_SIZE;
    }
}
