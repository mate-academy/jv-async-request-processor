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
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return cache.get(userId);
        }, executor).thenApply(
                userDetails -> {
                    if (userDetails != null) {
                        return userDetails;
                    } else {
                        cache.put(userId, new UserData(userId, "Details for " + userId));
                        return cache.get(userId);
                    }
                });
    }
}
