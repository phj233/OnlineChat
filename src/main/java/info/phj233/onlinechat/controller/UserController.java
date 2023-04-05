package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.model.dto.UserDTO;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.Result;
import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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
    private final UserService UserService;
    //注册
    @PostMapping("/register")
    public Result<Object> register(@RequestBody UserDTO user){
        try {
            if(ObjectUtils.isEmpty(user.getUsername()) || ObjectUtils.isEmpty(user.getPassword())){
                return ResultUtil.error(ResultEnum.PARAMS_ERROR, "用户名或密码不能为空");
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            if (UserService.register(user)){
                return ResultUtil.success("注册成功");
            }else {
                return ResultUtil.error(ResultEnum.OPERATION_ERROR, "注册失败");
            }
        }catch (Exception e) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, e.getMessage());
        }
    }
    @GetMapping("/test")
    public Result<Object> test() {
        return ResultUtil.success("测试成功");
    }
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
    @GetMapping("/checktoken")
    public Result<Object> checktoken(HttpServletRequest request) {
        String token = request.getHeader(JWTConfig.tokenHeader);
        if (!UserService.checkToken(token)) {
            return ResultUtil.error(ResultEnum.FORBIDDEN, "token已过期");
        }else {
            return ResultUtil.success("token未过期");
        }

    }

    //分页
    @GetMapping("/page")
    public Result<Object> pagefindAll(@RequestParam(value = "page", defaultValue = "2") Integer page,
                                      @RequestParam(value = "size", defaultValue = "1") Integer size) {
        return ResultUtil.success(UserService.pagefindAll(page, size));
    }



}
