package info.phj233.onlinechat;

import info.phj233.onlinechat.dao.UserDao;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineChatApplicationTests {
@Resource
private UserDao userDao;
    @Test
    void contextLoads() {

    }

}
