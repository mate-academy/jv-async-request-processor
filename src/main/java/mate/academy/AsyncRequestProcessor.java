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
        this.cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> fillAndReturnUserData(userId), executor);
    }

    private UserData fillAndReturnUserData(String userId) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        UserData userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);
        return userData;
    }
}
