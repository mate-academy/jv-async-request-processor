package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        this.cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            System.out.println("Retrieved from cache: " + userId);
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        return CompletableFuture.supplyAsync(() -> compute(userId), executor)
                .thenApply(userData -> {
                    cache.put(userId, userData);
                    return userData;
                });
    }

    private static UserData compute(String userId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
        return new UserData(userId, "Details for " + userId);
    }
}
