package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private static final long TIMEOUT = 1000;
    private static final String USER_DETAILS = "Details for %s";

    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() ->
           cache.computeIfAbsent(
                   userId, this::getUserData), executor
        );
    }

    private UserData getUserData(String userId) {
        try {
            TimeUnit.MILLISECONDS.sleep(TIMEOUT);
            return new UserData(userId, USER_DETAILS.formatted(userId));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
