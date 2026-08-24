package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;

    private final Map<String, CompletableFuture<UserData>> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        CompletableFuture<UserData> future = cache.computeIfAbsent(userId, ignored ->
                CompletableFuture.supplyAsync(() ->
                        new UserData(userId, "Details for " + userId), executor));
        future.whenComplete((userData, ex) -> {
            if (ex != null) {
                cache.remove(userId, future);
            }
        });
        return future;
    }
}
