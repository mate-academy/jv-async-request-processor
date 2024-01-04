package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mate.academy.UserData;
import mate.academy.service.UserDataService;

public class UserDataServiceMockImpl implements UserDataService {
    private final List<UserData> db = new ArrayList(
            Arrays.asList(new UserData("1", "Details"),
                    new UserData("2", "Details"),
                    new UserData("3", "Details"),
                    new UserData("4", "Details"))
    );

    @Override
    public UserData getUserById(String id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UserData userData = db.stream().filter(data -> data.userId() == id).findFirst().orElse(null);
        return userData;
    }
}
