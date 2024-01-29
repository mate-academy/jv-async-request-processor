package mate.academy;

import static java.util.concurrent.CompletableFuture.delayedExecutor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private static final int TIME_DELAY = 1;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        CompletableFuture<UserData> cacheLookupFuture = CompletableFuture.supplyAsync(() -> {
            if (!cache.containsKey(userId)) {
                return null;
            }
            return cache.get(userId);
        }, delayedExecutor(TIME_DELAY, TimeUnit.SECONDS));

        CompletableFuture<UserData> populateCacheFuture =
                cacheLookupFuture.thenCompose(existingUserData -> {
                    if (existingUserData != null) {
                        return CompletableFuture.completedFuture(existingUserData);
                    } else {
                        return CompletableFuture.supplyAsync(() -> {
                            UserData newUser = new UserData(
                                    userId,
                                    String.format("Details for user %s", userId)
                            );
                            cache.put(userId, newUser);
                            return newUser;
                        }, executor);
                    }
                });

        return populateCacheFuture.whenComplete((userData, throwable) -> {
            if (throwable != null) {
                System.err.printf("Error occurred while processing request for user %s: %s",
                        userId, throwable.getMessage());
                cache.remove(userId);
            } else {
                System.out.printf("Request processed successfully for user %s", userId);
            }
        });
    }
}
