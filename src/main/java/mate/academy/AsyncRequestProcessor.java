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
        for (String key : cache.keySet()) {
            if (key.equals(userId)) {
                return CompletableFuture.supplyAsync(() -> cache.get(key));
            } else {
                continue;
            }
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cache.put(userId, new UserData(userId, "Details for " + userId));
        return CompletableFuture.supplyAsync(() -> cache.get(userId));
    }
}
