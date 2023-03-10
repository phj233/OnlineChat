package info.phj233.onlinechat.util;

/**
 * 响应数据包装类
 * @author 未確認の庭師
 */
public class ResultUtil {
    public static <T> Result<T> success(T data) {
        ResultEnum r = ResultEnum.SUCCESS;
        return new Result<>(r.getCode(), r.getMsg(), data);
    }

    public static <T> Result<T> error(ResultEnum code) {
        return new Result<>(code.getCode(), code.getMsg(), null);
    }

    public static <T> Result<T> error(ResultEnum code, String msg) {
        return new Result<>(code.getCode(), msg, null);
    }
}
