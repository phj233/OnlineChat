package info.phj233.onlinechat.exception;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.exception
 * @className: LoginException
 * @author: phj233
 * @date: 2023/3/10 14:21
 * @version: 1.0
 */
public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
