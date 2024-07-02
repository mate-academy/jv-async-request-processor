package mate.academy;

import static javax.management.timer.Timer.ONE_SECOND;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String DETAILS_PREFIX = "Details for ";

    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            cache.computeIfAbsent(userId, id -> {
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
                return new UserData(id, DETAILS_PREFIX + id);
            });
            return cache.get(userId);
        }, executor);
    }
}
