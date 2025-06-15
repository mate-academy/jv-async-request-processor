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
        return CompletableFuture.supplyAsync(() -> {
            if (cache.containsKey(userId)) {
                return cache.get(userId);
            }
            return getUserById(userId);
        }, executor);
    }

    private UserData getUserById(String userId) {
        UserData userData = new UserData(userId, String.format("Details for %s", userId));
        cache.put(userId, userData);
        return userData;
    }
}
