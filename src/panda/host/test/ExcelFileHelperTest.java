package panda.host.test;

import org.junit.jupiter.api.Test;
import panda.host.model.models.User;
import panda.host.utils.Panda;
import panda.host.utils.ExcelFileHelper;

import java.util.List;
import java.util.Objects;

class ExcelFileHelperTest {

    @Test
    void getUserList() {
        List<User> users = Objects.requireNonNull(
                ExcelFileHelper.getUserList(Panda.DEFAULT_XLSX_FILE_PATH,
                        2));
        for (User user: users) {
            System.out.println(user);
        }
    }
}