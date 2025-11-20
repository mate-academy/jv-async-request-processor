package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cacheMap;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        cacheMap = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(
                () -> cacheMap.computeIfAbsent(userId, from()), executor);
    }

    private Function<String, UserData> from() {
        return userId -> new UserData(userId, "Details for " + userId);
    }
}
