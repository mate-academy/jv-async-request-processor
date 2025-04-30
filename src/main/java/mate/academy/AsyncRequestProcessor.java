package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    
    private final ConcurrentMap<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId));
        }

        CompletableFuture<UserData> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
                UserData userData = new UserData(userId, "User " + userId + " details...");
                cache.put(userId, userData);
                return userData;
            } catch (InterruptedException e) {
                throw new RuntimeException("InterruptedException occured!", e);
            }
        }, executor);

        return future;
    }
}
