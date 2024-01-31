package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        AsyncRequestProcessor asyncRequestProcessor = new AsyncRequestProcessor(executor);

        String[] userIds = {"user1", "user2", "user3", "user1"};
        CompletableFuture<?>[] futures = new CompletableFuture[userIds.length];

        for (int i = 0; i < userIds.length; i++) {
            String userId = userIds[i];
            futures[i] = asyncRequestProcessor.processRequest(userId)
                    .thenAccept(userData -> System.out.println("Processed: " + userData));
        }

        CompletableFuture.allOf(futures).join();
        executor.shutdown();
    }
}
