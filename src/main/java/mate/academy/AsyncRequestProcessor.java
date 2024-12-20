package mate.academy;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Random random = new Random();
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        try {
            Thread.sleep(random.nextInt(200));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        } else {
            UserData userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData);
            return userData;
        }
    }
}
