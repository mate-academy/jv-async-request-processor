package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<UserData>> inFlightCache =
            new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {

        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {

        UserData cachedData = cache.get(userId);

        if (cachedData != null) {
            return CompletableFuture.completedFuture(cachedData);
        }

        return inFlightCache.computeIfAbsent(userId, id -> CompletableFuture
                .supplyAsync(() -> fetchUserData(id), executor)
                .thenApply(userData -> {
                    cache.put(id, userData);
                    return userData;
                })
                .whenComplete((res, ex) -> inFlightCache.remove(id))
        );
    }

    private UserData fetchUserData(String userId) {

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while fetching user data");
        }

        return new UserData(userId, "Details for " + userId);
    }
}
