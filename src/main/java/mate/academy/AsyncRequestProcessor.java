package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final ConcurrentHashMap<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            if (cache.containsKey(userId)) {
                return cache.get(userId);
            }

            UserData userData = getUserDataFromBase(userId);
            cache.put(userId, userData);

            return userData;
        }, executor);
    }

    private UserData getUserDataFromBase(String userId) {
        try {
            Thread.sleep(500);
            return new UserData(userId, "Details for " + userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
