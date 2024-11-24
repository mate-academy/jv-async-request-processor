package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        // Check cache for the result
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        // Process the request asynchronously
        return CompletableFuture.supplyAsync(() -> {
            // Simulate a delay for processing the request
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Request processing interrupted", e);
            }

            // Create user data as a result of processing
            UserData userData = new UserData(userId, "Details for " + userId);

            // Store the result in the cache
            cache.put(userId, userData);
            return userData;
        }, executor);
    }
}
