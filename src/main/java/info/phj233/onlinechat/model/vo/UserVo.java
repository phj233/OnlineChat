package info.phj233.onlinechat.model.vo;

import lombok.Data;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.model.vo
 * @className: UserVo
 * @author: phj233
 * @date: 2023/3/31 22:06
 * @version: 1.0
 */
@Data
public class UserVo {
    private String username;
    private String password;
    private String email;
    private String avatar;
    private String token;
}
