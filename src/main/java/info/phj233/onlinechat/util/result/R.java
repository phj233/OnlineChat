package info.phj233.onlinechat.util.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 * @author phj233
 * @since 2023/5/30 20:55
 */
@Data
@AllArgsConstructor
public class R<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    /**
     * 创建一个不带数据的成功响应
     *
     * @param <T> 响应数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok() {
        return new R<>(E.SUCCESS.getCode(), "true", null);
    }

    /**
     * 创建一个带数据的成功响应
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return 带数据的成功响应
     */
    public static <T> R<T> ok(T data) {
        return new R<>(E.SUCCESS.getCode(), "true", data);
    }

    /**
     * 创建一个错误响应
     *
     * @param code 错误码枚举
     * @param <T>  响应数据类型
     * @return 错误响应
     */
    public static <T> R<T> error(E code) {
        return new R<>(code.getCode(), code.getMessage(), null);
    }

    /**
     * 创建一个自定义错误消息的错误响应
     * @param code 错误码枚举
     * @param message 错误消息描述
     * @return 错误响应
     */
    public static <T> R<T> error(E code, String message) {
        return new R<>(code.getCode(), message, null);
    }
    public static <T> R<T> error(int code, String message, T data) {
        return new R<>(code, message, data);
    }
}
