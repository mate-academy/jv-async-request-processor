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
            System.out.println("[CACHE HIT] Returning cached data for: " + cache.get(userId));
            return CompletableFuture.completedFuture(cache.get(userId));
        } else {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                UserData userData = new UserData(userId, "UserData details");
                cache.put(userId, userData);
                return userData;
            }, executor);
        }
    }
}
