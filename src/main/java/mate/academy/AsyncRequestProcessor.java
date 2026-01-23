package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private UserRepository userRepository = new UserRepositoryImpl();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> userRepository.findUserById(userId), executor)
                .thenApply(userData -> {
                    UserData existing = cache.putIfAbsent(userData.userId(), userData);
                    return existing != null ? existing : userData;
                });
    }
}
