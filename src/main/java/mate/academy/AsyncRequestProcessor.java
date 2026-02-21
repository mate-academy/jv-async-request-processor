package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData userData = cache.get(userId);
        if (userData != null) {
            return CompletableFuture.completedFuture(userData);
        }

        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    UserData newUserData = new UserData(userId, "Details for " + userId);
                    cache.put(userId, newUserData);
                    return newUserData;
                }, executor
        );
    }
}
