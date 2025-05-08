package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int TIME_DELAY = 1000;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cachedUserData = cache.get(userId);
        if (cachedUserData != null) {
            return CompletableFuture.completedFuture(cachedUserData);
        }

        return CompletableFuture.supplyAsync(() -> retrieveUserData(userId), executor)
                .whenComplete((userData, throwable) -> {
                    if (userData != null) {
                        cache.put(userId, userData);
                    }
                });
    }

    private UserData retrieveUserData(String userId) {
        try {
            Thread.sleep(TIME_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new UserData(userId, String.format("Details for user %s", userId));
    }
}
