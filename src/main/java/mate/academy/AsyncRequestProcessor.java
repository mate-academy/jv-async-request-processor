package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> cache.get(userId))
                .thenApply(data -> {
                    if (data == null) {
                        data = saveUserInfoToCache(userId);
                    }
                    return data;
                });
    }

    private UserData saveUserInfoToCache(String userId) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UserData userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);
        return userData;
    }
}
