package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.model.dto.UserDTO;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.Result;
import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.controller
 * @className: LoginAndRegisterController
 * @author: phj233
 * @date: 2023/3/10 8:59
 * @version: 1.0
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    /**
     * 注册
     * @param user 用户UserDTO
     * @return Result
     */
    @PostMapping("/register")
    public Result<Object> register(@RequestBody UserDTO user){
        try {
            if(ObjectUtils.isEmpty(user.getUsername()) || ObjectUtils.isEmpty(user.getPassword())){
                return ResultUtil.error(ResultEnum.PARAMS_ERROR, "用户名或密码不能为空");
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            if (userService.register(user)){
                return ResultUtil.success("注册成功");
            }else {
                return ResultUtil.error(ResultEnum.OPERATION_ERROR, "注册失败");
            }
        }catch (Exception e) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 测试
     * @return Result
     */
    @GetMapping("/test")
    public Result<Object> test() {
        return ResultUtil.success("测试成功");
    }

    /**
     * 获取用户信息
     * @param authentication 用户信息
     * @return Result
     */
    @GetMapping("/info")
    public Result<Object> info(Authentication authentication) {
        if (authentication == null) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, "未登录");
        }
        UserDetailImpl user = (UserDetailImpl) authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("userinfo", user.getUser());
        map.put("roles", user.getAuthorities());
        return ResultUtil.success(map);
    }

    /**
     * 验证token是否过期
     * @param request 请求
     * @return Result
     */
    @GetMapping("/checktoken")
    public Result<Object> checktoken(HttpServletRequest request) {
        String token = request.getHeader(JWTConfig.tokenHeader);
        if (!userService.checkToken(token)) {
            return ResultUtil.error(ResultEnum.FORBIDDEN, "token已过期");
        }else {
            return ResultUtil.success("token未过期");
        }

    }

    /**
     * 修改用户信息
     * @param user 用户信息
     * @return Result
     */
    @PreAuthorize("hasAnyRole('admin','user')")
    @PostMapping("/update")
    public Result<Object> update(@RequestBody User user) {
        try {
            if(ObjectUtils.isEmpty(user.getUsername()) || ObjectUtils.isEmpty(user.getPassword())){
                return ResultUtil.error(ResultEnum.PARAMS_ERROR, "用户名或密码不能为空");
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            if (userService.update(user)){
                return ResultUtil.success("修改成功");
            }else {
                return ResultUtil.error(ResultEnum.OPERATION_ERROR, "修改失败");
            }
        }catch (Exception e) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 修改用户头像
     * @param file 头像文件
     * @param authentication 用户信息
     * @param request 请求
     * @return Result
     */
    @PostMapping("/avatar")
    public Result<Object> avatar(@RequestParam("file")MultipartFile file, Authentication authentication, HttpServletRequest request) {
        if (authentication == null) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, "未登录");
        }
        if (file.isEmpty()) {
            return ResultUtil.error(ResultEnum.PARAMS_ERROR, "文件不能为空");
        }
        try{
            if (userService.updateAvatar(file, authentication, request)){
                return ResultUtil.success("上传成功");
            }else {
                return ResultUtil.error(ResultEnum.OPERATION_ERROR, "上传失败");
            }
        }catch (Exception e) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 分页获取所有用户
     * @return Result
     */
    @GetMapping("/page")
    public Result<Object> pagefindAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResultUtil.success(userService.pagefindAll(page, size));
    }



}
