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
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    public UserData getUserData(String userId) {
        if (cache.containsKey(userId)) {
            System.out.println("Taking data from cache...");
            return cache.get(userId);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Processor interrupted");
        }
        String details = "Details for " + userId;
        UserData userData = new UserData(userId, details);
        cache.put(userId, userData);
        return userData;
    }
}
