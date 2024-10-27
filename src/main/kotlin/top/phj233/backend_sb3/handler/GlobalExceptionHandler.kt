package top.phj233.backend_sb3.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import top.phj233.backend_sb3.util.ApiResponse
import top.phj233.backend_sb3.util.ResponseUtil

/**
 * 全局异常处理
 * 处理所有 Controller 层抛出的异常
 * 返回统一的 JSON 格式
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<String>> {
        ex.printStackTrace()
        return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message ?: "服务器内部错误")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<String>> {
        ex.printStackTrace()
        return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), ex.message ?: "非法参数")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<String>> {
        ex.printStackTrace()
        val errorMessage = ex.bindingResult.allErrors.joinToString(", ") { it.defaultMessage ?: "" }
        return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), errorMessage)
    }

}
