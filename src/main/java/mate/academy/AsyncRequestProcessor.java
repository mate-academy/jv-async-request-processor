package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> dataCache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (dataCache.containsKey(userId)) {
            return CompletableFuture.completedFuture(dataCache.get(userId));
        }
        return CompletableFuture
                .supplyAsync(() -> {
                    UserData userData = getUserData(userId);
                    dataCache.put(userId, userData);
                    return userData; },
                      executor);
    }

    private UserData getUserData(String userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new UserData(userId, "Details for " + userId);
    }
}
