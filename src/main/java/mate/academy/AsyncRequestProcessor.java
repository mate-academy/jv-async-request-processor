package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> cache.get(userId))
                .thenApply(data -> {
                    if (data == null) {
                        throw new ValueIsAbsentException();
                    }
                    return data;
                })
                .exceptionallyAsync(throwable -> handleThrowable(throwable, userId));
    }

    private static class ValueIsAbsentException extends RuntimeException {
    }

    private UserData handleThrowable(Throwable throwable, String userId) {
        if (throwable.getCause().getClass() == ValueIsAbsentException.class) {
            return saveUserInfoToCache(userId);
        }
        return null;
    }

    private UserData saveUserInfoToCache(String userId) {
        UserData userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);
        return userData;
    }
}
