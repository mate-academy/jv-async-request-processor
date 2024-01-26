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
        CompletableFuture<UserData> completableFuture = new CompletableFuture<>();

        completableFuture.completeAsync(() -> {
            if (cache.containsKey(userId)) {
                return cache.get(userId);
            }

            UserData userData = getUserDataFromBase(userId);
            cache.put(userId, userData);

            return userData;
        }, executor);
        return completableFuture;
    }

    private UserData getUserDataFromBase(String userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new UserData(userId, "Details for " + userId);
    }
}
