package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> map;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        map = new ConcurrentHashMap<>();

    }

    private static UserData apply(String id) {
        return new UserData(id, "Details for " + id);
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(
                () -> map.computeIfAbsent(userId, AsyncRequestProcessor::apply),
                executor);
    }
}
