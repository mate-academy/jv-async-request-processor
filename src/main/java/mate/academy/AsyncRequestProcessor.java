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
        UserData cachedData = cache.get(userId);
        if (cachedData != null) {
            return CompletableFuture.completedFuture(cachedData);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
                UserData userData = new UserData(userId, "Details for " + userId);
                cache.put(userId, userData);
                return userData;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);

    }
}
