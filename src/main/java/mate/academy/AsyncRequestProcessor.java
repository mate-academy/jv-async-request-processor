package mate.academy;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final ConcurrentMap<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserDataFromDB(userId), executor);
    }

    private UserData getUserDataFromDB(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }

        try {
            System.out.println("Retrieving data from db");
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            throw new RuntimeException("Couldn't get data from bd by userid: " + userId);
        }

        UserData userData = new UserData(userId, "Details for " + userId);
        cache.put(userId, userData);
        return userData;
    }
}
