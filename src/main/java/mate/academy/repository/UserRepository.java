package mate.academy.repository;

import mate.academy.UserData;

public interface UserRepository {
    UserData findUserById(String userId);
}
