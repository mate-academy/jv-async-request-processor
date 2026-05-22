package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        CompletableFuture<UserData> future = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                Thread.sleep(100);
                UserData data = new UserData(userId, "details");
                cache.put(userId, data);
                future.complete(data);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }
}
