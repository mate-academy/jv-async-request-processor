package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int PROCESSING_DELAY_MS = 200;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() ->
                cache.computeIfAbsent(userId, this::computeUserData), executor);
    }

    private UserData computeUserData(String userId) {
        try {
            Thread.sleep(PROCESSING_DELAY_MS);
            return new UserData(userId, "Details for " + userId);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to emulate user data", e);
        }
    }
}
