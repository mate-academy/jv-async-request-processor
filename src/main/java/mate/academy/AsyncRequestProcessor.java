package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String DETAILS_MESSAGE = "Details for %s";
    private final Map<String, UserData> cashData = new ConcurrentHashMap<>();
    private final Executor executor;

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
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task was interrupted", e);
            }
            return new UserData(userId, DETAILS_MESSAGE + userId);
        }, executor).thenApply(userData -> {
            cashData.put(userId, userData);
            return userData;
        });
    }
}
