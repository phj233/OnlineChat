package info.phj233.onlinechat.exception;

/**
 * 登录异常类
 * @author phj233
 * @since 2023/3/10 14:21
 * @version 1.0
 */
public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
