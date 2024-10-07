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

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // Simulated delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            UserData userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData); // Cache the result
            return userData;
        }, executor);
    }
}
