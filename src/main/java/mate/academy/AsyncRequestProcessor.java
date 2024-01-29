package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        this.cache = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return getUserData(userId);
        }
        return loadUserData(userId);
    }

    private CompletableFuture<UserData> loadUserData(String userId) {
        final String details = String.format("Details for %s", userId);

        return CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                TimeUnit.SECONDS.sleep(1L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return new UserData(userId, details);
                        }
                )
                .whenComplete((user, throwable) -> cache.put(userId, user));
    }

    private CompletableFuture<UserData> getUserData(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return cache.get(userId);
        });
    }
}
