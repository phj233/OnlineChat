package info.phj233.onlinechat.service.impl;

import com.auth0.jwt.JWT;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.model.dto.UserDTO;
import info.phj233.onlinechat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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
    public Boolean register(UserDTO user) {
        if (userDao.findUserByUsername(user.getUsername()) != null) {
            return false;
        }
        User userVO = new User(user);
        userDao.save(userVO);
        return true;
    }

    @Override
    public Boolean addUser(User user) {
        if (userDao.findUserByUsername(user.getUsername()) != null) {
            return false;
        }
        try {
            userDao.save(user);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    @Override
    public Boolean checkToken(String token) {
        if (token.startsWith(JWTConfig.tokenPrefix)){
            token = token.replace(JWTConfig.tokenPrefix, "");
        }
        return JWT.decode(token).getExpiresAt().getTime() > System.currentTimeMillis();
    }

    @Override
    public Boolean update(User user) {
        try {
            userDao.saveAndFlush(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传头像并更新数据库
     * @param file 头像文件
     * @param authentication 用户信息
     * @param request 请求
     * @return Boolean 是否成功
     */
    @Override
    public Boolean updateAvatar(MultipartFile file, Authentication authentication, HttpServletRequest request) {
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
            }else {
                return false;
            }
            userDao.saveAndFlush(user);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            userDao.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Page<User> pagefindAll(Integer page, Integer size) {
        try{
            Sort sort = Sort.by(Sort.Direction.ASC, "uid");
            PageRequest pageRequest = PageRequest.of(page, size, sort);
            return userDao.findAll(pageRequest);
        }catch (Exception e){
            log.info(e.getMessage());
            return null;
        }

    }


}
