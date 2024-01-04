package mate.academy.service.impl;

import mate.academy.UserData;
import mate.academy.service.UserDataService;

public class UserDataServiceMockImpl implements UserDataService {
    @Override
    public UserData getUserById(String id) {
        return new UserData(id, "Details");
    }
}
