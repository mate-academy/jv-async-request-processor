package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String USER_DETAILS = "Details for ";
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
                Thread.sleep(200);
                UserData userData = new UserData(userId, USER_DETAILS + userId);
                cache.put(userId, userData);
                return userData;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
