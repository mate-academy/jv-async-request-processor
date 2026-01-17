package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {

    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final UserDataDao dao;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        dao = new UserDataDao();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> cache.computeIfAbsent(
              userId,
              value -> dao.get(userId)
        ), executor);
    }
}
