package mate.academy;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            System.out.println("Cache hit for userId: " + userId);
            return CompletableFuture.supplyAsync(() -> cache.get(userId), executor);
        } else {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(450);  //simulate some work(e.g. fetch data from DB
                } catch (InterruptedException e) {
                    System.out.println("Error occurred!");
                }

                UserData newUser = new UserData(userId, getRandomDetails());
                cache.put(userId, newUser);
                System.out.println("Processed new request for userId: " + userId);
                return newUser;
            }, executor);
        }
    }

    private static String getRandomDetails() {
        String[] possibleDetails = {
                "Gold user", "Newbie user", "Super admin", "Boss", "Super hero", "Anime lover"
        };
        Random random = new Random();
        return possibleDetails[random.nextInt(possibleDetails.length)];
    }
}
