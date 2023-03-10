package info.phj233.onlinechat.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码枚举
 * @author 未確認の庭師
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(0, "success"),
    PARAMS_ERROR(4000, "参数错误"),
    NOT_LOGIN(4010, "未登录"),
    NO_AUTH(4011, "无权限"),
    NOT_FOUND(4040, "请求数据不存在"),
    FORBIDDEN(4030, "禁止访问"),
    SYSTEM_ERROR(5000, "系统内部异常"),
    OPERATION_ERROR(5001, "操作失败"),
    NOT_ACTIVE(5002, "用户未激活"),
    VERIFY_CODE_EXPIRED(5003, "验证码已过期");

    private final Integer code;
    private final String msg;
}
