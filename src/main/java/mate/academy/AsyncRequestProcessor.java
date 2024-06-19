package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        return CompletableFuture.supplyAsync(() -> {
            sleep(500);
            UserData userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData);
            return userData;
        }, executor);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupted!", e);
        }
    }
}
