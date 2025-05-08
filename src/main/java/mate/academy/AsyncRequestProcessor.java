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
        if (cache.containsKey(userId)) {
            System.out.println("Cache hit for userId: " + userId);
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                String details = "Details for " + userId;
                return new UserData(userId, details);
            } catch (InterruptedException e) {
                throw new RuntimeException("Request processing interrupted", e);
            }
        }, executor).thenApply(userData -> {
            cache.put(userId, userData);
            System.out.println("Processed: " + userData);
            return userData;
        }).exceptionally(ex -> {
            System.err.println("Failed to process request for userId: "
                    + userId
                    + ", Error: "
                    + ex.getMessage());
            return new UserData(userId, "Error occurred");
        });
    }
}
