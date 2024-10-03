package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    public Map<String, UserData> cashData = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cashData.containsKey(userId)) {
            return CompletableFuture.completedFuture(cashData.get(userId));
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new UserData(userId, "Details for " + userId);
        }, executor).thenApply(userData -> {
            cashData.put(userId, userData);
            return userData; // Return the UserData object
        });
    }
}
