package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int WAIT_MILLISECONDS = 1000;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getDetailsForUser(userId), executor);
    }

    private UserData getDetailsForUser(String userId) {
        try {
            Thread.sleep(WAIT_MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread" + Thread.currentThread().getName()
                                       + "was interrupted", e);
        }
        return cache.computeIfAbsent(userId, key ->
                new UserData(userId, "Details for " + userId));
    }
}
