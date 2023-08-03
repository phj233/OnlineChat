package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.model.value.Register;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.JWTUtil;
import info.phj233.onlinechat.util.result.E;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phj233
 * @since  2023/3/10 8:59
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    /**
     * 注册
     * @param register 注册信息
     * @return Result
     * @see Register
     */
    @PostMapping("/register")
    public R<User> register(@RequestBody Register register){
        return userService.register(register);
    }

    /**
     * 获取用户信息
     * @param authentication 用户信息
     * @return Result
     */
    @GetMapping("/info")
    public R<Object> info(Authentication authentication) {
        if (authentication == null) {
            return R.error(E.ACCESS_DENIED, "未认证");
        }
        UserDetailImpl user = (UserDetailImpl) authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("userinfo", user.getUser());
        map.put("roles", user.getAuthorities());
        return R.ok(map);
    }

    /**
     * 验证token是否过期
     * @param request 请求
     * @return Result
     */
    @GetMapping("/checktoken")
    public R<Boolean> checktoken(HttpServletRequest request) {
        String token = request.getHeader(JWTConfig.tokenHeader);
        return R.ok(JWTUtil.verifyToken(token));
    }

    /**
     * 修改用户信息
     * @param user 用户信息
     * @return Result
     */
    @PreAuthorize("hasAnyAuthority('admin', 'USER')")
    @PostMapping("/update")
    public R<User> update(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * 修改用户头像
     * @param file 头像文件
     * @param authentication 用户信息
     * @param request 请求
     * @return Result
     */
    @PostMapping("/avatar")
    public R<Object> avatar(@RequestParam("file")MultipartFile file, Authentication authentication, HttpServletRequest request) {
        return userService.updateAvatar(file, authentication, request);
    }

    /**
     * 分页获取所有用户
     * @return Result
     */
    @GetMapping("/page")
    public R<Page<User>> pagefindAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.pagefindAll(page, size);
    }



}
