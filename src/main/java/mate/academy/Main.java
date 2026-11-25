package mate.academy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Feel free to play with AsyncRequestProcessor in this main method if you want
        ExecutorService executor = Executors.newFixedThreadPool(5);
        AsyncRequestProcessor asyncRequestProcessor = new AsyncRequestProcessor(executor);

        // Simulating multiple concurrent requests
        String[] userIds = {"user1", "user2", "user3", "user1"}; // Note: "user1" is repeated
        List<CompletableFuture<UserData>> futures = new ArrayList<>();

        for (String userId : userIds) {
            futures.add(asyncRequestProcessor.processRequest(userId));
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Print results in the same order as the input userIds
        for (CompletableFuture<UserData> future : futures) {
            System.out.println("Processed: " + future.join());
        }

        executor.shutdown();
    }
}
