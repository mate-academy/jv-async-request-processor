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
            return CompletableFuture.completedFuture(cache.get(userId));
        } else {
            CompletableFuture<UserData> future = new CompletableFuture<>();
            executor.execute(() -> {
                try {
                    String details = "Details for " + userId;
                    cache.put(userId, new UserData(userId, details));
                    Thread.sleep(500);
                    future.complete(cache.get(userId));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            return future;
        }
    }
}
