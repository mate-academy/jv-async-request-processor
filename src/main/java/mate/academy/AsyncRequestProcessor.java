package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache
            = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cachedData = cache.get(userId);
        if (cachedData != null) {
            return CompletableFuture.completedFuture(cachedData);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // Затримка 1 секунда
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Processing interrupted", e);
            }

            UserData userData = new UserData(userId, "Details for " + userId);

            cache.put(userId, userData);

            return userData;
        }, executor);
    }
}
