package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {

    private final Executor executor;
    private final Map<String, CompletableFuture<UserData>> cache = new ConcurrentHashMap<>();
    private final UserDataDao dao;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        dao = new UserDataDao();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return cache.computeIfAbsent(userId, key -> {
            CompletableFuture<UserData> future = CompletableFuture.supplyAsync(
                    () -> dao.get(userId), executor);

            future.whenComplete((result, exception) -> {
                if (exception != null) {
                    cache.remove(userId, future);
                }
            });

            return future;
        });
    }
}
