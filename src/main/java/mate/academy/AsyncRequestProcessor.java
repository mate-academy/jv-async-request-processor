package mate.academy;

import java.util.Map;
import java.util.Optional;
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
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(cache.get(userId)), executor)
                .thenCompose(optional -> optional.map(CompletableFuture::completedFuture)
                        .orElseGet(() -> CompletableFuture.supplyAsync(() -> getUserData(userId))))
                .exceptionally(e -> {
                    System.out.println(e.getMessage());
                    return null;
                });
    }

    private UserData getUserData(String userId) {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        UserData userData = new UserData(userId, String.valueOf(userId.hashCode()));
        cache.put(userId, userData);
        return userData;
    }
}

