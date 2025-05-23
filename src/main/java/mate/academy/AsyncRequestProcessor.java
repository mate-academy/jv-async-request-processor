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
        UserData cachedData = cache.get(userId);
        if (cachedData == null) {
            return CompletableFuture.supplyAsync(() -> {
                simulateDelay(); // Simulate I/O
                UserData userData = new UserData(userId, "Details for " + userId);
                cache.put(userId, userData);
                return userData;
            }, executor);
        }
        return CompletableFuture.completedFuture(cachedData);

    }

    private void simulateDelay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
