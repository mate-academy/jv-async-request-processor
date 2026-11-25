package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<UserData>> inFlightRequests
            = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cachedUserData = cache.get(userId);
        if (cachedUserData != null) {
            return CompletableFuture.completedFuture(cachedUserData);
        }

        CompletableFuture<UserData> request = inFlightRequests.computeIfAbsent(userId, id -> {
            UserData cached = cache.get(id);
            if (cached != null) {
                return CompletableFuture.completedFuture(cached);
            }

            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }

                return new UserData(id, "Details for " + id);
            }, executor).thenApply(userData -> {
                cache.put(id, userData);
                return userData;
            });
        });

        request.whenComplete((userData, error) -> {
            inFlightRequests.remove(userId, request);
        });

        return request;
    }
}
