package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> cache;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            UserData userData = new UserData(userId, "user " + userId);
            cache.put(userId, userData);
            return userData;
        }, executor);
    }
}
