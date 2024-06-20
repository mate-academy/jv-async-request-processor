package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> userDataMap = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (userDataMap.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> userDataMap.get(userId),
                    CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS, executor));
        } else {
            userDataMap.put(userId, new UserData(userId, "info about user with id: " + userId));
            return CompletableFuture.supplyAsync(() -> userDataMap.get(userId), executor);
        }
    }
}
