package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        var data = new UserData(userId, "Details for " + userId);
        return CompletableFuture.supplyAsync(() -> {
            cache.computeIfAbsent(userId, userDetails -> data);
            return data;
        }, executor);
    }
}
