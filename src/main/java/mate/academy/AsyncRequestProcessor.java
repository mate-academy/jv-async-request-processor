package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int SLEEPING_DURATION = 500;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId), executor);
        }
        try {
            Thread.sleep(SLEEPING_DURATION);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UserData userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);
        return CompletableFuture.supplyAsync(() -> userData, executor);
    }
}
