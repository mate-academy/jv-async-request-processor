package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String DETAILS_FOR = "Details for ";
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture
                    .supplyAsync(() -> cache.get(userId), executor);
        }
        return CompletableFuture
                .supplyAsync(() -> findUserDataFromDB(userId), executor)
                .whenComplete((userData, throwable) -> cache.put(userId, userData));
    }

    private UserData findUserDataFromDB(String userId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new UserData(userId, DETAILS_FOR + userId);
    }
}
