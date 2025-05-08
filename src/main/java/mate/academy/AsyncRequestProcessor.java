package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        return cache.containsKey(userId) ? cache.get(userId) : collectUserData(userId);
    }

    private UserData collectUserData(String userId) {
        mockDelay(1000);
        UserData collectedUserData = new UserData(userId, userId + " details mock");
        cache.put(userId, collectedUserData);
        return collectedUserData;
    }

    private static void mockDelay(long ms) {
        try {
            Thread.sleep(ms); //mock delay
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
