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
            return CompletableFuture.supplyAsync(() -> cache.get(userId));
        } else {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException("Something was wrong!", e);
            }
            String details = "Details for " + userId;
            UserData userData = new UserData(userId, details);
            cache.put(userId, userData);
            return CompletableFuture.supplyAsync(() -> new UserData(userId, details));
        }
    }
}
