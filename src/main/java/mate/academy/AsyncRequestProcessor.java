package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, CompletableFuture<UserData>> cache;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return cache.computeIfAbsent(userId, id ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        UserData userData = new UserData(userId, "Details for " + id);
                        Thread.sleep(1000);
                        return userData;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Request for " + id + " was interrupted");
                    }
                },executor)
        );
    }
}
