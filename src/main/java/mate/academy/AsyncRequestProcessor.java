package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int MILLISECONDS_SLEEP = 500;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (userId == null || userId.isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("userId must not be null or empty"));
        }

        return CompletableFuture.supplyAsync(() ->
                cache.computeIfAbsent(userId, this::getDataForUser), executor);
    }

    private UserData getDataForUser(String userId) {
        sleep(MILLISECONDS_SLEEP);
        return new UserData(userId, "Details for " + userId);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
