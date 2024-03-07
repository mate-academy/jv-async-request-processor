package mate.academy;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache;
    private final Random random;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        this.cache = new ConcurrentHashMap<>();
        this.random = new Random();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(
                () -> getUserDataIfPresentOrCreate(userId), executor);
    }

    private UserData getUserDataIfPresentOrCreate(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        try {
            Thread.sleep(random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        UserData user = new UserData(userId, "Details for " + userId);
        cache.put(userId,user);
        return user;
    }
}
