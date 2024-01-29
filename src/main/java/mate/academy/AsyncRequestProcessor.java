package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    public static final int TIMEOUT_TIME = 500;
    public static final String USER_DETAILS_TEXT = "Details for ";
    private Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        try {
            TimeUnit.MILLISECONDS.sleep(TIMEOUT_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (cache.containsKey(userId)) {
            return CompletableFuture
                    .supplyAsync(() -> cache.get(userId), executor);
        }

        UserData userData = new UserData(userId, USER_DETAILS_TEXT + userId);

        return CompletableFuture
                .supplyAsync(() -> userData, executor)
                .whenComplete((result, throwable) -> {
                    if (throwable == null) {
                        cache.put(userId, result);
                    } else {
                        throw new RuntimeException(throwable);
                    }
                });
    }
}

