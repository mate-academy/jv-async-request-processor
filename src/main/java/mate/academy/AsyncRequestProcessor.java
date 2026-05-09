package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;

    private final Map<String, UserData> cache =
            new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {

        // Return cached value immediately
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(
                    cache.get(userId)
            );
        }

        // Asynchronous processing
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simulate database/API delay
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            UserData userData =
                    new UserData(userId,
                            "Details for " + userId);

            // Save to cache
            cache.put(userId, userData);

            return userData;
        }, executor);
    }
}

