package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, CompletableFuture<UserData>> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        // Use computeIfAbsent to initiate the data-fetching CompletableFuture only if absent
        return cache.computeIfAbsent(userId, id -> fetchUserDataAsync(id));
    }

    // Asynchronous method to simulate fetching data with delay
    private CompletableFuture<UserData> fetchUserDataAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simulate a delay for fetching user data
                TimeUnit.SECONDS.sleep(1); // Simulate delay without blocking the cache
            } catch (InterruptedException e) {
                // Reset interrupt flag and throw RuntimeException to indicate cancellation
                Thread.currentThread().interrupt();
                throw new RuntimeException("Operation was interrupted for userId: " + userId, e);
            }
            return new UserData(userId, "Details for " + userId);
        }, executor);
    }
}
