package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, CompletableFuture<UserData>> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // Simulating a delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String userDetails = "Details for " + userId;
            UserData userData = new UserData(userId, userDetails);
            CompletableFuture<UserData> future = CompletableFuture.completedFuture(userData);
            cache.put(userId, future);
            return userData;
        }, executor);
    }
}
