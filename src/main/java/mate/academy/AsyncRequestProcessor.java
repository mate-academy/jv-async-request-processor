package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final long MOCK_WORK_TIME = 500;

    private final Executor executor;
    private Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            if (cache.containsKey(userId)) {
                return cache.get(userId);
            }

            try {
                Thread.sleep(MOCK_WORK_TIME);

                String details = "Details for " + userId;
                UserData userData = new UserData(userId, details);

                cache.put(userId, userData);

                return userData;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Request processing was interrupted while "
                        + "fetching data for userId: " + userId, e);
            }
        }, executor);
    }
}
