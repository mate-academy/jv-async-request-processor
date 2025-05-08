package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    public static final String DETAILS_HEAD = "Details for ";
    public static final int TIMEOUT_SIMULATION = 2000;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        return cache.computeIfAbsent(userId, this::fetchUserDataById);
    }

    private UserData fetchUserDataById(String userId) {
        // TODO: real user data fetching needs to be implemented
        simulateApiRequest(userId);
        return new UserData(userId, DETAILS_HEAD + userId);
    }

    private static void simulateApiRequest(String userId) {
        try {
            long timeout = ThreadLocalRandom.current().nextLong(TIMEOUT_SIMULATION);
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            System.out.println("Interrupted for " + userId);
            Thread.currentThread().interrupt();
        }
    }
}
