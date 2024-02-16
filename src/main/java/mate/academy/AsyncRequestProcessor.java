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

        UserData cachedResult = cache.get(userId);
        if (cachedResult != null) {
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
            Thread.currentThread().interrupt();
        }
        return new UserData(userId, "details: " + userId);
    }
}
