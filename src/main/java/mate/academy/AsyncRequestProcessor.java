package mate.academy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        UserData userData1 = new UserData("user1","some details 1");
        UserData userData2 = new UserData("user2","some details 2");
        UserData userData3 = new UserData("user3","some details 3");
        Map<String, String> databaseMock = new HashMap<>();
        databaseMock.put(userData1.userId(), userData1.details());
        databaseMock.put(userData2.userId(), userData2.details());
        databaseMock.put(userData3.userId(), userData3.details());

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); //Mocking request DB
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserData userData = new UserData(userId,
                    databaseMock.getOrDefault(userId, "Unknown user"));
            cache.put(userId, userData);
            return userData;
        }, executor);
    }
}
