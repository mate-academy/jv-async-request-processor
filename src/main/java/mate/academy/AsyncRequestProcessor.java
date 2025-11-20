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
        // The processing can be a simulated task,
        // like fetching user data from a database (mock this with a delay).

        // According to the requirements,even fetching might be delayed.
        // Actually, as in real applications!
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return cacheMap.computeIfAbsent(userId, from());
                }, executor)
                .exceptionally(throwable -> from().apply("Default user"));
    }

    private Function<String, UserData> from() {
        return userId -> new UserData(userId, "Details for " + userId);
    }
}
