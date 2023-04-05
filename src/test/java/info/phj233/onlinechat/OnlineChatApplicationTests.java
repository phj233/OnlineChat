package info.phj233.onlinechat;

import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.dto.UserDTO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineChatApplicationTests {
@Resource
private UserDao userDao;
@Resource
private info.phj233.onlinechat.service.UserService UserService;

    @Test
    void addUser(){
       UserDTO userDTO =  new UserDTO();
       userDTO.setUsername("test");
       userDTO.setPassword("test");
       userDTO.setEmail("test");
       UserService.register(userDTO);
    }

    @Test
    void findUserByUsername(){
        System.out.println(userDao.findUserByUsername("test"));
    }

    @Test
    // 更新用户信息
    void updateUser(){
        User user = new User();
        user.setUid(10);
        user.setEmail("test");
        user.setUsername("test");
        user.setPassword("test");
        user.setRole("admin");
        user.setEnabled(true);

        System.out.println(UserService.updateUser(user));
    }

    @Test
    void deleteUser(){
        System.out.println(UserService.deleteById(4));
    }


}
