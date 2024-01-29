package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (cache.containsKey(userId)) {
            return CompletableFuture
                    .supplyAsync(() -> cache.get(userId), executor);
        }

        UserData userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);

        return CompletableFuture
                .supplyAsync(() -> userData, executor);
    }
}

