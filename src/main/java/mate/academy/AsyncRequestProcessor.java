package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String USER_DETAIL = "user number is %s";
    private static final String SLEEP_EXCEPTION = "InterruptedException";
    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId));
        }
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(SLEEP_EXCEPTION);
                    }
                    return new UserData(userId, String.format(USER_DETAIL, userId));
                }
        ).whenComplete((user, throwable) -> cache.put(userId, user));
    }
}
