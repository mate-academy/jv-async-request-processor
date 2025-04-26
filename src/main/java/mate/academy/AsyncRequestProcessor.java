package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private ConcurrentHashMap<String, UserData> map;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        map = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() ->
                map.computeIfAbsent(userId, id -> new UserData(id, "Details for " + id)), executor);
    }
}
