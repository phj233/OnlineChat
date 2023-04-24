package info.phj233.onlinechat.model.dto;

import lombok.Data;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.model.dto
 * @className: UserDTO
 * @author: phj233
 * @date: 2023/3/31 22:09
 * @version: 1.0
 */
@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
}
