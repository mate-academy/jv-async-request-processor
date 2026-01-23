package mate.academy;

public interface UserRepository {
    UserData findUserById(String userId);
}
