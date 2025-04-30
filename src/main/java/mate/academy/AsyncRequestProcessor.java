package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import mate.academy.exception.RequestProcessingException;

public class AsyncRequestProcessor {
    private static final int TASK_EMULATION_DELAY = 1000;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() ->
                cache.computeIfAbsent(userId, this::emulateDataBaseAccess), executor);
    }

    private UserData emulateDataBaseAccess(String userId) {
        try {
            Thread.sleep(TASK_EMULATION_DELAY);
            return new UserData(userId, "Some %s user data".formatted(userId));
        } catch (InterruptedException e) {
            throw new RequestProcessingException("Failed to emulate user data", e);
        }
    }
}
