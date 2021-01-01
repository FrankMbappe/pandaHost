package panda.host.test;

import org.junit.jupiter.api.Test;
import panda.host.model.models.User;
import panda.host.utils.UserXLSHelper;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserXLSHelperTest {

    @Test
    void getUserList() {
        List<User> users = Objects.requireNonNull(
                UserXLSHelper.getUserList(UserXLSHelper.DEFAULT_FILE_PATH,
                        2));
        for (User user: users) {
            System.out.println(user);
        }
    }
}