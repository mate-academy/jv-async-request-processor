package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, CompletableFuture<UserData>> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return cache.computeIfAbsent(userId, key ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new UserData(userId, "Details for " + userId);
                }, CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS, executor))
        );
    }
}
