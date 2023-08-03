package info.phj233.onlinechat.service;

import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.value.Register;
import info.phj233.onlinechat.service.impl.UserServiceImpl;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户服务接口
 * @author phj233
 * @see UserServiceImpl
 * @since  2023/3/10 9:37
 * @version 1.0
 */
public interface UserService {
    R<User> register(Register register);

    R<User> addUser(User user);

    R<Page<User>>pagefindAll(Integer page, Integer size);

    R<User> update(User user);

    R<Object> updateAvatar(MultipartFile file, Authentication authentication, HttpServletRequest request);
}
