package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> caches = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getDataByUserId(userId));
    }

    private UserData getDataByUserId(String userId) {
        if (caches.containsKey(userId)) {
            return caches.get(userId);
        } else {
            UserData userData = new UserData(userId, "Details for " + userId);
            caches.put(userId, userData);
            return caches.get(userId);
        }
    }
}
