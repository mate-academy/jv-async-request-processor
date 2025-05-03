package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    public static final int WAIT_TIME_MILLISECONDS = 1000;
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        UserData userData = cache.get(userId);
        if (userData != null) {
            return userData;
        }

        wait(WAIT_TIME_MILLISECONDS);
        userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);
        return userData;
    }

    private void wait(int waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupted!", e);
        }
    }
}
