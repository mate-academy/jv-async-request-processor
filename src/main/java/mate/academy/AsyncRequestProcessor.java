package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String USER_DETAILS = "Details for ";
    private static final int DEFAULT_SLEEPING_TIME = 200;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId), executor);
        }

        return CompletableFuture
                .supplyAsync(() -> {
                    try {
                        Thread.sleep(DEFAULT_SLEEPING_TIME);
                        return new UserData(userId, USER_DETAILS + userId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }, executor)
                .whenComplete((userData, throwable) -> {
                    if (throwable == null) {
                        cache.put(userId, userData);
                    } else {
                        throw new RuntimeException(throwable.getMessage());
                    }
                });
    }
}
