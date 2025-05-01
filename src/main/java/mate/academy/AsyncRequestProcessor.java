package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        return CompletableFuture.supplyAsync(() -> {
            UserData userData = fetch(userId);
            cache.put(userId, userData);
            return userData;
        }, executor);
    }

    private static UserData fetch(String userId) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException("process failed");
        }
        return new UserData(userId, "details");
    }
}
