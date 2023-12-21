package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Function<String, UserData> createUserDetails;
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        createUserDetails = userId -> new UserData(userId, "Details for " + userId);
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> fetchUserData(userId), executor)
                .exceptionally(throwable -> {
                    System.out.println(throwable);
                    return null;
                });
    }

    private UserData fetchUserData(String userId) {
        try {
            TimeUnit.MILLISECONDS.sleep(300);

            return cache.computeIfAbsent(userId, createUserDetails);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Userdata fetching failed");
        }
    }
}
