package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class AsyncRequestProcessor {
    private static final String USER_DATA_DETAILS = "Details for %s";
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId));
        }
        return process(userId);
    }

    private CompletableFuture<UserData> process(String userId) {
        return CompletableFuture
                .supplyAsync(() -> getAppliedUserData(userId), executor)
                .whenComplete((userData, throwable) -> cache.put(userId, userData));
    }

    private UserData getAppliedUserData(String userId) {
        return getUserDataFunction().apply(userId);
    }

    private Function<String, UserData> getUserDataFunction() {
        return userId -> new UserData(userId, String.format(USER_DATA_DETAILS, userId));
    }
}
