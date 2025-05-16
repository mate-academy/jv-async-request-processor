package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    // Кеш для збереження оброблених запитів
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        // Якщо дані для userId вже є в кеші - повертаємо їх негайно
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }

        // Обробка запиту асинхронно з використанням заданого Executor
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Імітація затримки для обробки запиту (наприклад, виклик до бази даних)
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Створюємо об'єкт UserData з даними користувача
            UserData userData = new UserData(userId, "Details for " + userId);
            // Додаємо результат у кеш
            cache.put(userId, userData);
            return userData;
        }, executor);
    }
}
