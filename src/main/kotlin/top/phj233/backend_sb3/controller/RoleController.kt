package top.phj233.backend_sb3.controller

import cn.dev33.satoken.annotation.SaCheckRole
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/role")
@SaCheckRole("admin")
class RoleController {

}
