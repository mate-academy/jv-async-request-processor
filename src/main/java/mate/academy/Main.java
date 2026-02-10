package mate.academy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        AsyncRequestProcessor processor = new AsyncRequestProcessor(executor);

        String[] userIds = {"user1", "user2", "user3", "user1"};

        for (String userId : userIds) {
            processor.processRequest(userId)
                    .thenAccept(result ->
                            System.out.println("Processed: " + result));
        }

        executor.shutdown();
    }
}
