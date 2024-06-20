package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import mate.academy.exception.NoUserInCacheException;
import mate.academy.exception.RequestProcessingException;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(
                () -> findUserInCache(userId), executor).exceptionally(ex -> {
                    try {
                        UserData userData = emulateDataBaseAccess(userId);
                        cacheFetchedData(userId, userData);
                        return userData;
                    } catch (InterruptedException e) {
                        throw new RequestProcessingException("Failed to process "
                                + "the request for userId=" + userId, e);
                    }
                });
    }

    private UserData findUserInCache(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        } else {
            throw new NoUserInCacheException();
        }
    }

    private void cacheFetchedData(String userId, UserData userData) {
        cache.put(userId, userData);
    }

    private UserData emulateDataBaseAccess(String userId) throws InterruptedException {
        return new UserData(userId, "Some %s user data".formatted(userId));
    }
}
