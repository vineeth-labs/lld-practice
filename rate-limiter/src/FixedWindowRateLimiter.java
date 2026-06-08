import model.WindowInfo;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        WindowInfo windowInfo = userWindowInfo.get(userId);
        if (windowInfo == null) {
            // no entry yet - create one with single request
            WindowInfo created = new WindowInfo(windowId, 1);
            userWindowInfo.put(userId, created);
            return true;
        }

        long existingWindowId = windowInfo.getWindowId();
        if (existingWindowId != windowId) {
            // window rolled over - reset counter for this user
            userWindowInfo.put(userId, new WindowInfo(windowId, 1));
            return true;
        }

        int requestCount = windowInfo.getRequestCount();
        if (requestCount >= REQUEST_LIMIT) {
            return false;
        }

        windowInfo.incrementRequestCount();
        return true;
    }

    private long computeWindowId(Instant timestamp) {
        return timestamp.getEpochSecond() / DEFAULT_WINDOW_SIZE;
    }
}
