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

        return CompletableFuture
                .supplyAsync(() -> fetchUserData(userId), executor)
                .thenApply(userData -> {
                    cache.put(userId, userData);
                    return userData;
                });
    }

    private UserData fetchUserData(String userId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted for " + userId, e);
        }
        return new UserData(userId, "Details for " + userId);
    }
}
