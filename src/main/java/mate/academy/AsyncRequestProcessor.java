package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new UserData(userId, "Details");
        }, executor);
    }
}
