package mate.academy;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Random random = new Random();
    private Map<String, UserData> map = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (map.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> map.get(userId));
        }
        UserData userData = new UserData(userId, "This user from "
                + Country.values()[random.nextInt(Country.values().length)]);
        map.put(userId, userData);
        return CompletableFuture.supplyAsync(() -> map.get(userId));
    }
    enum Country {
        UKRAINE,
        USA,
        UK,
        CANADA,
        MOLDOVA,
        QATAR
    }
}
