package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cached = cache.get(userId);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }

        return fetchUserDataAsync(userId)
                .thenApply(userData -> saveToCache(userId, userData));
    }

    private CompletableFuture<UserData> fetchUserDataAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDatabaseCall();
            return new UserData(userId, "Details for " + userId);
        }, this.executor);
    }

    private UserData saveToCache(String userId, UserData userData) {
        cache.put(userId, userData);
        return userData;
    }

    private void simulateDatabaseCall() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Data fetching interrupted", e);
        }
    }
}
