package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
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

        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        return CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return cache.computeIfAbsent(userId, key -> new UserData(key, "Details for " + key));
        }, executor);
    }

    private void sleep(int time) {
        ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);
        ScheduledFuture<Void> delayFuture = delayExecutor
                .schedule(() -> null, time, TimeUnit.MILLISECONDS);
        try {
            delayFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        } finally {
            delayExecutor.shutdownNow();
        }
    }
}