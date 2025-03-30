package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, String> cache;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        this.cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId))
                    .thenApply(data -> new UserData(userId, data));
        } else {
            CompletableFuture<UserData> future = new CompletableFuture<>();
            executor.execute(() -> {
                String details = "Details for " + userId;
                cache.put(userId, details);
                future.complete(new UserData(userId, cache.get(userId)));
            });

            return future;
        }
    }
}
