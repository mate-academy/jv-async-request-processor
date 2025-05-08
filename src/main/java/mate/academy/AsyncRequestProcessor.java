package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(
                    () -> cache.get(userId),
                    executor
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interruption occurred", e);
            }

            return new UserData(userId, "Details for " + userId);
        }, executor)
                .whenComplete((userData, throwable) -> {
                    if (throwable == null) {
                        cache.put(userId, userData);
                    } else {
                        throw new RuntimeException(
                                "Cannot process the request for " + userId, throwable);
                    }
                });
    }
}
