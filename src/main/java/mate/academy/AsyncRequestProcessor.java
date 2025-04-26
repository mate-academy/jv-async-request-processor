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
        if (map.get(userId) != null) {
            return CompletableFuture.supplyAsync(() -> map.get(userId));
        }
        UserData userData = new UserData(userId, "Processed");
        CompletableFuture<UserData> completableFuture = CompletableFuture.supplyAsync(() ->
                userData);
        map.put(userId, userData);
        return completableFuture;
    }
}
