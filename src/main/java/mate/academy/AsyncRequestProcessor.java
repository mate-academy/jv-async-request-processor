package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import mate.academy.repository.UserRepository;
import mate.academy.repository.UserRepositoryImpl;

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
                    addToCache(userData);
                    return userData;
                });
    }

    private void addToCache(UserData userData) {
        cache.put(userData.userId(), userData);
    }
}
