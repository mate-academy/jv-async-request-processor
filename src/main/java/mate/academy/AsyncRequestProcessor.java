package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache;

    {
        cache = new ConcurrentHashMap<>();
    }

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData userData = cache.get(userId);
        if (userData != null) {
            return CompletableFuture.completedFuture(userData);
        }

        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        try {
            Thread.sleep(700);
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
