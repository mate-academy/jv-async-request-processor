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
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        return CompletableFuture
                .supplyAsync(() -> {
                    simulateDelay();
                    return new UserData(userId, "Details for " + userId);
                }, executor)
                .thenApply(userData -> {
                    cache.put(userId, userData);
                    return userData;
                });
    }

    private void simulateDelay() {
        try {
            Thread.sleep(1_000); // 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
