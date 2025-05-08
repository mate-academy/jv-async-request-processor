package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, CompletableFuture<UserData>> cache =
            new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!cache.containsKey(userId)) {
            CompletableFuture<UserData> userDataCompletableFuture = create(userId);
            cache.put(userId, userDataCompletableFuture);
            return userDataCompletableFuture;
        }
        return cache.get(userId);
    }

    private static CompletableFuture<UserData> create(String userId) {
        String userDetails = String.format("Details for %s", userId);
        return CompletableFuture.supplyAsync(() -> new UserData(userId, userDetails));
    }
}
