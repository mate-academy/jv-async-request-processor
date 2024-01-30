package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cachedResult = cache.get(userId);
        if (cachedResult != null) {
            System.out.println("Served from cache: " + cachedResult);
            return CompletableFuture.completedFuture(cachedResult);
        }
        return CompletableFuture.supplyAsync(() -> simulateProcessing(userId), executor)
                .thenApply(result -> {
                    cache.put(userId, result);
                    return result;
                });
    }

    private UserData simulateProcessing(String userId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String details = "Details for " + userId;
        return new UserData(userId, details);
    }
}
