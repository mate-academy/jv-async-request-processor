package mate.academy;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private Map<String, UserData> map;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        map = new ConcurrentHashMap<>();
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(
                () -> map.computeIfAbsent(userId, this::apply),
                executor);
    }

    private UserData apply(String id) {
        int toSleep = new Random().nextInt(100);
        try {
            Thread.sleep(toSleep);
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't sleep", e);
        }
        return new UserData(id, "Details for " + id);
    }
}
