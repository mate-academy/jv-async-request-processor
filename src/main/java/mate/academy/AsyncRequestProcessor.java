package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final int DB_ACCESS_DELAY = 500;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        return CompletableFuture.supplyAsync(() -> {
            simulateDatabaseAccess();
            UserData userData = new UserData(userId, "Details for " + userId);
            cache.put(userId, userData);
            return userData;
        }, executor);
    }

    private static void simulateDatabaseAccess() {
        try {
            Thread.sleep(DB_ACCESS_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread "
                    + Thread.currentThread().getName()
                    + " was interrupted during simulated database access delay", e);
        }
    }
}
