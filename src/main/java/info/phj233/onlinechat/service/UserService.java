package info.phj233.onlinechat.service;

import info.phj233.onlinechat.model.dto.UserDTO;


/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.service
 * @className: UserService
 * @author: phj233
 * @date: 2023/3/10 9:37
 * @version: 1.0
 */
public interface UserService {
    Boolean register(UserDTO user);
    Boolean checkToken(String token);
}
