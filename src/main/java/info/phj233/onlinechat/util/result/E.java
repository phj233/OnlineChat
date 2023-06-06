package info.phj233.onlinechat.util.result;

import lombok.Getter;

/**
 * @author phj233
 * @since 2023/5/30 20:57
 */
@Getter
public enum E {
    SUCCESS(200, "成功"),
    USERNAME_PASSWORD_EMPTY(400, "用户名或密码为空"),
    USERNAME_PASSWORD_ERROR(400, "用户名或密码错误"),
    USERNAME_ALREADY_EXIST(400, "用户名已存在"),
    EMAIL_ALREADY_EXIST(400, "邮箱已存在"),
    PHONE_ALREADY_EXIST(400, "手机号已存在"),
    INVALID_CREDENTIALS(401, "无效的凭证"),
    ACCESS_DENIED(403, "访问被拒绝"),
    RESOURCE_NOT_FOUND(404, "未找到资源"),
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    ERROR_DELETE(500, "删除失败"),
    ERROR_NOT_FOUND(500, "未找到"),
    ERROR(500, "错误"),
    ERROR_UNKNOWN(500, "未知错误"),
    FILE_EMPTY(400, "文件为空"),
    USER_NOT_LOGIN( 400, "用户未登录"),
    USER_NOT_EXIST(400, "用户不存在"),
    USER_LOCKED(400, "用户被锁定"),
    TOKEN_EXPIRED( 400, "token过期"),;
    private final int code;
    private final String message;

    E(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

