package top.phj233.backend_sb3.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import top.phj233.backend_sb3.model.User
import kotlin.reflect.KClass

interface UserRepository : KRepository<User, Long> {
    fun findUserByUsername(username: String): User?
    fun findUserByEmail(email: String): User?
    fun findUserByEmail(email: String, fetcher: Fetcher<User>): User?
    fun <V: View<User>> findByEmail(email: String? = null ,viewType: KClass<V>): V?
    fun <V: View<User>> findByUsernameLike(username: String? ,pageOf: PageRequest,viewType: KClass<V>): Page<V>?
}
