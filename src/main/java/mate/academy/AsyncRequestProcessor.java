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
        UserData userData = cache.get(userId);
        if (userData != null) {
            return CompletableFuture.completedFuture(userData);
        }

        return CompletableFuture.supplyAsync(
                () -> cache.computeIfAbsent(userId, this::processUserData),
                executor
        );
    }

    private UserData processUserData(String userId) {
        try {
            Thread.sleep(1000); // Simulate processing delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new UserData(userId, "Details for " + userId);
    }
}
