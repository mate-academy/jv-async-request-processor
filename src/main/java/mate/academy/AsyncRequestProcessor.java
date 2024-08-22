package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final Map<String, UserData> CACHE = new ConcurrentHashMap<>();
    private static final String DETAILS_FOR = "Details for %s";
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        CACHE.computeIfAbsent(userId, (userData) ->
                new UserData(userId, String.format(DETAILS_FOR, userId)));
        return new CompletableFuture<UserData>().completeAsync(() -> CACHE.get(userId), executor);
    }
}
