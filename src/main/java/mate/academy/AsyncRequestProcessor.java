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
        UserData userData = cache.get(userId);
        if (userData == null) {
            return CompletableFuture
                .supplyAsync(() -> createUserData(userId), executor)
                .thenApply(data -> {
                    cache.put(userId, data);
                    return data;
                });
        } else {
            return CompletableFuture.completedFuture(
                new UserData(userId, "Details of user: " + userId));
        }
    }

    private UserData createUserData(String userId) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new UserData(userId, "Details of user: " + userId);
    }
}
