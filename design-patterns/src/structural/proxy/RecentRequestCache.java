import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class RecentRequestCache {

    private final Map<String, Entry> entries = new HashMap<>();

    private final long ttlSeconds;

    public RecentRequestCache(long ttlSeconds) {

        this.ttlSeconds = ttlSeconds;

    }


    public void put(String requestId, String response) {
        synchronized (entries) {

            entries.put(requestId, new Entry(response, System.currentTimeMillis()));
        }

    }


    public String get(String requestId) {

        removeExpired();


        return entries.getOrDefault(requestId, new Entry(null, 0L)).response;

    }


    private void removeExpired() {

        long now = System.currentTimeMillis();
        for (Map.Entry<String, Entry> item : entries.entrySet()) {

            if (now - item.getValue().createdAt > ttlSeconds * 1000) {
                entries.remove(item.getKey());
            }

        }


    }



    private static class Entry {

        private final String response;

        private final long createdAt;



        private Entry(String response, long createdAt) {

            this.response = response;

            this.createdAt = createdAt;

        }

    }

}