package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<UserData>> inFlight = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        return inFlight.computeIfAbsent(userId, id -> CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task was interrupted for user " + id, e);
            }
            return new UserData(id, "Details for " + id);
        }, executor)
                .whenComplete((userData, ex) -> {
                    if (ex == null) {
                        cache.put(id, userData);
                    }
                    inFlight.remove(id);
                })
        );
    }
}
