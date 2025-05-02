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
            return CompletableFuture.supplyAsync(() -> cache.get(userId));
        }
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor)
                .whenComplete((userData, throwable) -> cache.put(userId, userData));
    }

    private UserData getUserData(String userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new UserData(userId, String.format("Details for user: %s", userId));
    }
}
