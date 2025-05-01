package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final long DEFAULT_TIME_FOR_SLEEP = 200;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(
                    () -> cache.get(userId), executor
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(DEFAULT_TIME_FOR_SLEEP);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new UserData(userId,
                    String.format("Details for user%s", userId)
            );
        }, executor).whenComplete((userData, throwable) -> {
            if (throwable == null) {
                cache.put(userId, userData);
            }
        }).exceptionally(throwable -> {
            throw new RuntimeException(throwable.getMessage());
        });
    }
}
