package info.phj233.onlinechat.service.impl;

import com.auth0.jwt.JWT;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.model.value.Register;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.result.E;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystems;


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
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    @Override
    public R<User> register(Register register) {
        if (register.username().isEmpty() || register.password().isEmpty()) {
            return R.error(E.USERNAME_PASSWORD_EMPTY);
        }
        User user = new User();
        user.setUsername(register.username());
        user.setPassword(new BCryptPasswordEncoder().encode(register.password()));
        user.setRole("user");
        user.setEnabled(true);
        user.setEmail(register.email());
        try {
            user= userDao.saveAndFlush(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error(E.USERNAME_ALREADY_EXIST);
        }
        return R.ok(user);
    }

    @Override
    public R<User> addUser(User user) {
        if (userDao.findUserByUsername(user.getUsername()) != null) {
            return R.error(E.USERNAME_ALREADY_EXIST);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        try {
            user = userDao.saveAndFlush(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error(E.ERROR, e.getMessage());
        }
        return R.ok(user);

    }
    @Override
    public Boolean checkToken(String token) {
        if (token.startsWith(JWTConfig.tokenPrefix)){
            token = token.replace(JWTConfig.tokenPrefix, "");
        }
        return JWT.decode(token).getExpiresAt().getTime() > System.currentTimeMillis();
    }

    @Override
    public R<User> update(User user) {
        if (StringUtils.hasText(user.getUsername()) || StringUtils.hasText(user.getPassword())) {
            return R.error(E.USERNAME_PASSWORD_EMPTY);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        try {
            user = userDao.saveAndFlush(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return R.ok(user);
    }

    /**
     * 上传头像并更新数据库
     * @param file 头像文件
     * @param authentication 用户信息
     * @param request 请求
     * @return Boolean 是否成功
     */
    @Override
    public R<Object> updateAvatar(MultipartFile file, Authentication authentication, HttpServletRequest request) {
        if (file.isEmpty()) {
            return R.error(E.FILE_EMPTY);
        }
        if (authentication == null) {
            return R.error(E.USER_NOT_LOGIN);
        }
        UserDetailImpl authenticationPrincipal = (UserDetailImpl) authentication.getPrincipal();
        User user = authenticationPrincipal.getUser();
        try {
            //接收文件并存在 upload文件夹下，avatarPath为文件在服务器上的路径
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                String avatarName = user.getUsername() + "-" +user.getUid() + fileName.substring(fileName.lastIndexOf("."));
                file.transferTo(FileSystems.getDefault().getPath(System.getProperty("user.dir") + "/upload/avatar",avatarName));
                String avatarPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/upload/avatar/" + avatarName;
                user.setAvatar(avatarPath);
            }
            User saved = userDao.saveAndFlush(user);
            return R.ok(saved);
        } catch (Exception e) {
            log.info(e.getMessage());
            return R.error(E.ERROR, e.getMessage());
        }
    }

    @Override
    public R<Page<User>> pagefindAll(Integer page, Integer size) {
        try{
            Sort sort = Sort.by(Sort.Direction.ASC, "uid");
            PageRequest pageRequest = PageRequest.of(page, size, sort);
            Page<User> users = userDao.findAll(pageRequest);
            return R.ok(users);
        }catch (Exception e){
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }


}
