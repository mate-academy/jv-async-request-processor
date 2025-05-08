package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Function<String, UserData> userDataFunction;
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
        userDataFunction = userId -> new UserData(userId, "Details for " + userId);
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (!cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> userDataFunction.apply(userId), executor)
                    .whenComplete((userData, exception) -> {
                        if (exception != null) {
                            throw new RuntimeException(exception);
                        }
                        cache.put(userId, userData);
                    });
        }
        return CompletableFuture.supplyAsync(() -> cache.get(userId));
    }
}
