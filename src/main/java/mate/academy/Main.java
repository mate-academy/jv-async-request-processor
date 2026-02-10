package mate.academy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        AsyncRequestProcessor processor = new AsyncRequestProcessor(executor);

        String[] userIds = {"user1", "user2", "user3", "user1"};
        CompletableFuture<UserData>[] futures =
                new CompletableFuture[userIds.length];

        for (int i = 0; i < userIds.length; i++) {
            futures[i] = processor.processRequest(userIds[i]);
        }

        CompletableFuture.allOf(futures).join();

        for (CompletableFuture<UserData> future : futures) {
            System.out.println("Przetworzono: " + future.join());
        }

        executor.shutdown();
    }
}
