package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache;
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this(executor, new ConcurrentHashMap<>());
    }

    private AsyncRequestProcessor(Executor executor, Map<String, UserData> cache) {
        this.executor = executor;
        this.cache = cache;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        return createCompletableFuture(userId);
    }

    private CompletableFuture<UserData> createCompletableFuture(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserData userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData);
            return userData;
        }, executor);
    }
}
