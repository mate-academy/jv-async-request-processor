package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final ConcurrentHashMap<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return cache.get(userId) != null ? CompletableFuture.completedFuture(cache.get(userId)) :
                CompletableFuture.supplyAsync(() -> getUserDataByUserId(userId), executor)
                        .whenCompleteAsync((u, ex) -> {
                            if (ex != null) {
                                throw new RuntimeException();
                            }
                            cache.put(u.userId(), u);
                        });
    }

    public UserData getUserDataByUserId(String userId) {
        return new UserData(userId, "Details for %s".formatted(userId));
    }
}
