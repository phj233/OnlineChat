package top.phj233.backend_sb3.util

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
)
