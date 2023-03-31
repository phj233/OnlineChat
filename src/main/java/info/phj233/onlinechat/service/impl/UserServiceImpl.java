package info.phj233.onlinechat.service.impl;

import com.auth0.jwt.JWT;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.dto.UserDTO;
import info.phj233.onlinechat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.service.impl
 * @className: UserServiceImpl
 * @author: phj233
 * @date: 2023/3/10 9:50
 * @version: 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    @Override
    public Boolean register(UserDTO user) {
        if (userDao.findUserByUsername(user.getUsername()) != null) {
            return false;
        }
        User userVO = new User(user);
        userDao.save(userVO);
        return true;
    }

    @Override
    public Boolean checkToken(String token) {
        if (token.startsWith(JWTConfig.tokenPrefix)){
            token = token.replace(JWTConfig.tokenPrefix, "");
        }
        return JWT.decode(token).getExpiresAt().getTime() < System.currentTimeMillis();
    }
}
