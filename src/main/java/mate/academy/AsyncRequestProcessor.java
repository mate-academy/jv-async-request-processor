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
        this.random = new Random();
        this.cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> fetchUserDetail(userId), executor);
    }

    public UserData fetchUserDetail(String userId) {
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (cache.containsKey(userId)) {
            // get from cache
            return cache.get(userId);
        }

        UserData user = new UserData(userId, "Details for " + userId);

        // add to cache
        cache.put(userId, user);

        return user;
    }
}
