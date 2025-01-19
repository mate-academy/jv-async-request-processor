package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        String details = "Details for " + userId;
        UserData userData = new UserData(userId, details);
        return CompletableFuture.supplyAsync(() -> userData, executor);
    }
}
