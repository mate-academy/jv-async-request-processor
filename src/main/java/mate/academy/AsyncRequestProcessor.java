package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache;

    {
        cache = new ConcurrentHashMap<>();
    }

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cachedUserData = cache.get(userId);
        if (cachedUserData != null) {
            return CompletableFuture.completedFuture(cachedUserData);
        }

        return CompletableFuture.supplyAsync(() -> {
            UserData userData = getUserData(userId);
            cache.put(userId, userData);
            return userData;
        }, executor);
    }

    private UserData getUserData(String userId) {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException("Oops, something went wrong", e);
        }

        return new UserData(userId, "Details for " + userId);
    }

    public static void main(String[] args) {
        Executor executor = Runnable::run;
        AsyncRequestProcessor processor = new AsyncRequestProcessor(executor);
        String[] userIds = {"user1", "user2", "user3", "user1"};
        for (String userId : userIds) {
            processor.processRequest(userId).thenAccept(userData ->
                    System.out.println("Processed: " + userData)
            );
        }
    }
}
