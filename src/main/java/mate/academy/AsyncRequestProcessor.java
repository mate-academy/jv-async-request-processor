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
        UserData userData = cache.computeIfAbsent(
                userId, this::createUserData);

        return CompletableFuture.supplyAsync(
                () -> userData, executor);
    }

    private UserData createUserData(String userId) {
        return new UserData(userId, "Details for " + userId);
    }
}
