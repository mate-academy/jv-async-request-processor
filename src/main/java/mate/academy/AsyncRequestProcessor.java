package mate.academy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> userCache = new HashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            if (userCache.containsKey(userId)) {
                return userCache.get(userId);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                UserData userData = new UserData(userId, "details for " + userId);
                userCache.put(userId, userData);
                return userData;
            }
        }, executor);
    }
}
