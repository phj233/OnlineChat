package info.phj233.onlinechat.service.impl;

import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.dto.User;
import info.phj233.onlinechat.service.LoginAndRegisterService;
import lombok.RequiredArgsConstructor;


/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.service.impl
 * @className: LoginAndRegisterServiceImpl
 * @author: phj233
 * @date: 2023/3/10 9:50
 * @version: 1.0
 */
@RequiredArgsConstructor
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService {
    private final UserDao userDao;
    @Override
    public Boolean register(User user) {
        userDao.save(user);
        return userDao.findUserByUsername(user.getUsername()) != null;
    }

    @Override
    public Boolean login(User user) {
        return userDao.findUserByUsername(user.getUsername()) != null;
    }
}
