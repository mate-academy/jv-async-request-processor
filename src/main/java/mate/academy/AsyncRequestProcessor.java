package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;

    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        try {
            Thread.sleep(100);
            if (cache.containsKey(userId)) {
                return CompletableFuture.completedFuture(cache.get(userId));
            }
            UserData userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData);
            return CompletableFuture.supplyAsync(() -> userData, executor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
