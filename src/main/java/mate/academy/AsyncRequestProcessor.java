package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    public static final int TIMEOUT = 1;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture
                .supplyAsync(
                        () -> cache.computeIfAbsent(userId, this::getUserData),
                        executor
                );
    }

    private UserData getUserData(String userId) {
        try {
            TimeUnit.SECONDS.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    "Thread was interrupted while getting user data for userId: " + userId, e
            );
        }
        return new UserData(userId, "Data for user %s".formatted(userId));
    }
}
