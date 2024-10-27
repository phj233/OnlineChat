package top.phj233.backend_sb3.config

import cn.dev33.satoken.stp.StpInterface
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import top.phj233.backend_sb3.model.dto.UserView
import top.phj233.backend_sb3.repository.UserRepository

/**
 * @author phj233
 * @since 2024/10/15 21:25
 * @version
 */
@Component
class StpInterfaceImpl(val userRepository: UserRepository) : StpInterface {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    override fun getPermissionList(loginId: Any?, loginType: String?): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun getRoleList(loginId: Any?, loginType: String?): MutableList<String> {
        userRepository.viewer(UserView::class).findNullable(loginId.toString().toLong())?.let {
            log.info("getRoleList: ${it.roles}")
            return it.roles.map { roles ->
                roles.name
            }.toMutableList()
        }
        return mutableListOf()
    }

}
