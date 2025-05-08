package mate.academy;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String DETAILS_MESSAGE = "Details for %s";
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            UserData userData = cache.get(userId);
            return userData != null ? userData : write(userId);
        }, executor);
    }

    private UserData write(String userId) {
        try {
            Thread.sleep((new Random().nextInt(9) + 1) * 100);
        } catch (InterruptedException e) {
            throw new RuntimeException("Something went wrong...", e);
        }
        cache.put(userId, new UserData(userId, DETAILS_MESSAGE.formatted(userId)));
        return cache.get(userId);
    }
}
