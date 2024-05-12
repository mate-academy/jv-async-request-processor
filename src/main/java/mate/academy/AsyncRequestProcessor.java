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
        cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            UserData cachedUserData = cache.get(userId);
            if (cachedUserData != null) {
                return cachedUserData;
            }
            UserData userData = new UserData(
                    userId,
                    "Details for " + userId
            );
            cache.put(userId, userData);
            return userData;
        }, executor);
    }
}
