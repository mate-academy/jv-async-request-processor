package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String SIMULATE_DETAIL_INFO = "Details for ";
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        UserData userData = getSimuleteUserData(userId);
        return CompletableFuture.supplyAsync(() -> {
            cache.put(userId, userData);
            return userData;
        }, executor);
    }

    private UserData getSimuleteUserData(String userId) {
        simulateDelay();
        return new UserData(userId, SIMULATE_DETAIL_INFO + userId);
    }

    private void simulateDelay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while simulating delay");
        }
    }
}
