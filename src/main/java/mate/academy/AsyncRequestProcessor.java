package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        Map<String, UserData> userDataMap = new ConcurrentHashMap<>();

        if (userDataMap.containsKey(userId)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            return CompletableFuture.supplyAsync(() -> userDataMap.get(userId));
        } else {
            userDataMap.put(userId, new UserData(userId, "info about user with id: " + userId));
        }

        return CompletableFuture.supplyAsync(() -> userDataMap.get(userId));
    }
}
