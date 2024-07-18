package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> map = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (map.containsKey(userId)) {
            return getUser(userId,executor);

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        map.put(userId,new UserData(userId,"some details"));
        return getUser(userId,executor);
    }

    private CompletableFuture<UserData> getUser(String userId, Executor executor) {
        return CompletableFuture.supplyAsync(() ->
                new UserData(userId,
                        "Some details about " + userId),executor);
    }
}
