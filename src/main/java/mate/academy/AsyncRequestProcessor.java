package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static final String INTERRUPTED_EXCEPTION_MESSAGE =
            "Interrupted exception";
    private static final String USER_DETAILS_MESSAGE = "Some details about user";
    private static final int SLEEP_TIME_MILLIS = 500;
    private final Executor executor;
    private final Map<String, UserData> appCache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (appCache.containsKey(userId)) {
            return getUserData(userId);
        }
        return createUserData(userId);
    }

    private CompletableFuture<UserData> createUserData(String userId) {
        return CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                Thread.sleep(SLEEP_TIME_MILLIS);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(INTERRUPTED_EXCEPTION_MESSAGE, e);
                            }
                            return new UserData(userId, USER_DETAILS_MESSAGE);
                        }
                )
                .whenComplete((user, throwable) -> appCache.put(userId, user));
    }

    private CompletableFuture<UserData> getUserData(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(SLEEP_TIME_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(INTERRUPTED_EXCEPTION_MESSAGE, e);
            }
            return appCache.get(userId);
        });
    }
}
