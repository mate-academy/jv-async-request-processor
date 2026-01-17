package mate.academy;

public class UserDataDao {

    public UserData get(String userId) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new UserData(userId, "Details for " + userId);
    }
}
