package mate.academy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new HashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cachedResult = cache.get(userId);
        if (cachedResult != null) {
            System.out.println("Served from cache: " + cachedResult);
            return CompletableFuture.completedFuture(cachedResult);
        }

        return CompletableFuture.supplyAsync(() -> {
            simulateProcessing();

            UserData userData = new UserData(userId, "Details for " + userId);

            cache.put(userId, userData);

            System.out.println("Processed: " + userData);
            return userData;
        }, executor);
    }

    private void simulateProcessing() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while processing request", e);
        }
    }
}
