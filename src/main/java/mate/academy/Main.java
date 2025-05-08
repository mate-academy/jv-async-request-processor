package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final int THREADS_COUNT = 5;

    public static void main(String[] args) {
        String[] userIds = {"user1", "user2", "user3", "user1"};
        CompletableFuture<?>[] futures = new CompletableFuture[userIds.length];
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);

        try {
            AsyncRequestProcessor asyncRequestProcessor = new AsyncRequestProcessor(executor);
            for (int i = 0; i < userIds.length; i++) {
                String userId = userIds[i];
                futures[i] = asyncRequestProcessor.processRequest(userId)
                        .thenAccept(userData -> System.out.println("Processed: " + userData));
            }
            CompletableFuture.allOf(futures).join();
        } finally {
            executor.shutdown();
        }
    }
}
