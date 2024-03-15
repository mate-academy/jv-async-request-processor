package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cashUserData;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        this.cashUserData = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> cashUserData.get(userId), executor)
                .thenApply(userData -> {
                    if (userData == null) {
                        userData = mockGetFromDb(userId);
                    }
                    return userData;
                });
    }

    private UserData mockGetFromDb(String userId) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UserData userData = new UserData(userId, userId);
        cashUserData.put(userId, userData);
        return userData;
    }
}
