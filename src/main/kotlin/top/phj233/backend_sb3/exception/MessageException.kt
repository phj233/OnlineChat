package top.phj233.backend_sb3.exception

/**
 * @author phj233
 * @since 2024/10/25 17:58
 * @version
 */
class MessageException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
