package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private static final String USER_DETAILS = "Details for %s";
    private static final long TIMEOUT = 1000L;

    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        return cache.computeIfAbsent(userId, s -> {
            requestProcessingSimulation(userId);
            return new UserData(userId, USER_DETAILS.formatted(userId));
        });
    }

    private static void requestProcessingSimulation(String userId) {
        try {
            long timeout = ThreadLocalRandom.current().nextLong(TIMEOUT);
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException ex) {
            System.out.printf("Thread %s is interrupted for user %s%n",
                    Thread.currentThread().getName(), userId);
            Thread.currentThread().interrupt();
        }
    }
}
