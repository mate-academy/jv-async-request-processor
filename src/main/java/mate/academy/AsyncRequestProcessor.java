package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {

    private final Executor executor;

    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<UserData>> inFlight =
            new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cached = cache.get(userId);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }

        return inFlight.computeIfAbsent(userId, id ->
                CompletableFuture.supplyAsync(() -> {
                    simulateDelay();
                    return new UserData(id, "Details for " + id);
                }, executor).whenComplete((result, error) -> {
                    inFlight.remove(id);
                    if (error == null) {
                        cache.put(id, result);
                    }
                })
        );
    }

    private void simulateDelay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
