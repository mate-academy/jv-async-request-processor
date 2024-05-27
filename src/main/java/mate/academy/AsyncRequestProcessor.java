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
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        UserData userData = cache.get(userId);
        if (userData != null) {
            return userData;
        }

        try {
            Thread.sleep(1000);
            userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData);
            return userData;
        } catch (InterruptedException e) {
            throw new RuntimeException("Something went wrong!");
        }
    }
}
