package mate.academy.repository;

import mate.academy.UserData;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public UserData findUserById(String userId) {
        try {
            Thread.sleep(200);
            return new UserData(userId, "Details for " + userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
