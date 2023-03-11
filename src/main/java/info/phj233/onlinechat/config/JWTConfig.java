package info.phj233.onlinechat.config;

import lombok.Data;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.config
 * @className: JWTConfig
 * @author: phj233
 * @date: 2023/3/10 23:43
 * @version: 1.0
 */
@Data
public class JWTConfig {
    /**
     * 密钥
     */
    public String secret;
    /**
     * 过期时间30分钟
     */
    public static Long expiration;
    /**
     * Token前缀
     */
    public static String tokenPrefix;
    /**
     * TokenKey
     */
    public static String tokenHeader;
    /**
     * 不需要认证的路径
     */
    public String antMatchers;

}
