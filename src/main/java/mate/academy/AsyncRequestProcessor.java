package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId));
        }
        UserData userData = new UserData(userId,"Details for " + userId);
        cache.put(userId,userData);
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return userData;
        });
    }
}
