package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int SLEEP_FOR_1_SEC = 1000;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId), executor);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(SLEEP_FOR_1_SEC);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserData userData = new UserData(userId,String.format("Details for %s", userId));
            return userData;
        }).whenComplete((userData, throwable) -> cache.put(userId, userData));
    }
}
