package top.phj233.backend_sb3.controller

import cn.dev33.satoken.annotation.SaCheckRole
import cn.dev33.satoken.annotation.SaIgnore
import cn.dev33.satoken.secure.BCrypt
import cn.dev33.satoken.stp.StpUtil
import jakarta.servlet.http.HttpServletRequest
import org.babyfish.jimmer.client.EnableImplicitApi
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.phj233.backend_sb3.exception.MessageException
import top.phj233.backend_sb3.model.dto.UserLogin
import top.phj233.backend_sb3.model.dto.UserRegister
import top.phj233.backend_sb3.model.dto.UserUpdate
import top.phj233.backend_sb3.model.dto.UserView
import top.phj233.backend_sb3.repository.UserRepository
import top.phj233.backend_sb3.util.ApiResponse
import top.phj233.backend_sb3.util.ResponseUtil

@EnableImplicitApi
@RestController
@RequestMapping("/user")
class UserController(
    val userRepository: UserRepository,
) {
    val log:Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 登录
     * @param userLogin 用户登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @SaIgnore
    fun login(@RequestBody userLogin : UserLogin): ResponseEntity<ApiResponse<UserView>> {
        log.info("login: $userLogin")
        val userEntity = userRepository.findUserByEmail(userLogin.email)
        if (userEntity != null) {
            if (BCrypt.checkpw(userLogin.password,userEntity.password)) {
                userEntity.email.compareTo(userLogin.email).let {
                    if (it != 0) {
                        throw RuntimeException("邮箱错误")
                    }
                }
                StpUtil.login(userEntity.id)
                val userView = userRepository.findByEmail(userLogin.email, UserView::class)
                return ResponseUtil.success(userView)
            }
        }
        throw RuntimeException("用户名或密码错误")
    }

    @PostMapping("/register")
    @SaIgnore
    fun register(@RequestBody user : UserRegister): ResponseEntity<ApiResponse<String>> {
        val userEntity = userRepository.findUserByUsername(user.username)
        userEntity?.let {
            throw RuntimeException("用户已存在")
        }
        user.copy(password = BCrypt.hashpw(user.password)).let {
            userRepository.save(it,SaveMode.INSERT_ONLY)
            return ResponseUtil.success("注册成功")
        }
    }

    @GetMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse<String>> {
        StpUtil.logout()
        return ResponseUtil.success("登出成功")
    }

    @GetMapping("/info")
    fun info(request: HttpServletRequest): ResponseEntity<ApiResponse<UserView>> {
        log.info(StpUtil.getTokenInfo().toString())
        val userView = userRepository.findByEmail(StpUtil.getLoginIdAsString(), UserView::class)
        return ResponseUtil.success(userView)
    }

    @GetMapping("/list")
    fun list(@RequestParam(defaultValue = "0") page: Int): ResponseEntity<ApiResponse<Page<UserView>>> {
        val users = userRepository.viewer(UserView::class).findAll(PageRequest.of(
            page, 10
        ))
        return ResponseUtil.success(users)
    }

    /**
     * 模糊搜索用户
     * @param username 用户名
     * @return List<UserView> 用户列表
     */
    @GetMapping("/like_search")
    fun likeSearch(@RequestParam username: String,@RequestParam(defaultValue = "0") page: Int): ResponseEntity<ApiResponse<Page<UserView>>> {
        val users = userRepository.findByUsernameLike(username, PageRequest.of(
            page, 10
        ), UserView::class)
        return ResponseUtil.success(users)
    }

    /**
     * 更新用户信息
     * @param userUpdate 用户信息
     * @return UserView 用户信息
     */
    @PutMapping("/update")
    fun update(@RequestBody userUpdate: UserUpdate): ResponseEntity<ApiResponse<UserView>> {
        if (StpUtil.hasRole("admin") || userUpdate.id == StpUtil.getLoginIdAsLong()){
            userUpdate.copy(password = BCrypt.hashpw(userUpdate.password)).let {
                userRepository.update(it)
            }
            return ResponseUtil.success(userUpdate.id?.let { userRepository.viewer(UserView::class).findNullable(it) })
        }
        throw MessageException("权限不足")
    }

    /**
     * 删除用户
     * @param id 用户id
     */
    @SaCheckRole("admin")
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ApiResponse<String>> {
        val user = userRepository.findById(id).orElse(null)
        user?.let {
            userRepository.delete(it)
            return ResponseUtil.success("删除成功")
        }
        throw MessageException("用户不存在")
    }

}
