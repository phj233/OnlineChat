package top.phj233.backend_sb3.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ResponseUtil {

    /**
     * 构建一个成功的响应
     * @param data 数据
     * @param <T> 数据类型
     * @return ResponseEntity<ApiResponse<T>>
     */
    fun <T> success(data: T?): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.ok(ApiResponse(200, "success", data))
    }

    /**
     * 构建一个错误的响应
     *
     * @param code    错误码
     * @param message 错误信息
     * @param <T>     数据类型
     * @return ResponseEntity<ApiResponse<T>>
     */
    fun <T> error(code: Int, message: String): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(HttpStatus.valueOf(code)).body(ApiResponse(code, message, null))
    }

    /**
     * 构建一个自定义的响应
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return ResponseEntity<ApiResponse<T>>
     */
    fun <T> custom(code: Int, message: String, data: T?): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(HttpStatus.valueOf(code)).body(ApiResponse(code, message, data))
    }
}
