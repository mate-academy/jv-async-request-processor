package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException("This thread was interrupted.");
        }
        return CompletableFuture.supplyAsync(() -> getUserFromMap(userId), executor);
    }

    private UserData getUserFromMap(String key) {
        return (cache.containsKey(key)) ? cache.get(key) : addUserToMap(key);
    }

    private UserData addUserToMap(String userId) {
        cache.put(userId, new UserData(userId, "Details for " + userId));
        return cache.get(userId);
    }
}
