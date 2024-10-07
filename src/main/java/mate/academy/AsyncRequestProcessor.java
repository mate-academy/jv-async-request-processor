package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, CompletableFuture<UserData>> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        CompletableFuture<UserData> userDataFuture = fetchUserDataAsync(userId);
        return cache.computeIfAbsent(userId, id -> userDataFuture);
    }

    private CompletableFuture<UserData> fetchUserDataAsync(String userId) {
        Executor delayedExecutor = CompletableFuture.delayedExecutor(
                1, TimeUnit.SECONDS, executor
        );

        return CompletableFuture.supplyAsync(
                () -> new UserData(userId, "Details for " + userId), delayedExecutor
        ).exceptionally(ex -> {
            System.out.println(
                    "Failed to process request for userId: " + userId + " - " + ex.getMessage()
            );
            return null;
        });
    }
}
