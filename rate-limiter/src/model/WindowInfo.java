package model;

import java.util.concurrent.atomic.AtomicInteger;

public class WindowInfo {
    private final long windowId;
    private final AtomicInteger requestCount;

    public WindowInfo(long windowId, int requestCount) {
        this.windowId = windowId;
        this.requestCount = new AtomicInteger(requestCount);
    }

    public long getWindowId() {
        return windowId;
    }

    public int getRequestCount() {
        return requestCount.get();
    }

    public int incrementRequestCount() {
        return requestCount.incrementAndGet();
    }
}
