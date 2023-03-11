package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.model.dto.User;
import info.phj233.onlinechat.service.LoginAndRegisterService;
import info.phj233.onlinechat.util.Result;
import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.controller
 * @className: LoginAndRegisterController
 * @author: phj233
 * @date: 2023/3/10 8:59
 * @version: 1.0
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final LoginAndRegisterService loginAndRegisterService;
    //注册
    @PostMapping("/register")
    public Result<Object> register(User user){
        if(user.getUsername().isEmpty()||user.getPassword().isEmpty()){
            return ResultUtil.error(ResultEnum.PARAMS_ERROR, "用户名或密码不能为空");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if (loginAndRegisterService.register(user)){
            return ResultUtil.success("注册成功");
        }else {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, "注册失败");
        }
    }
    @PostMapping("/login")
    public Result<Object> login(User user) {
        if(user.getUsername().isEmpty()||user.getPassword().isEmpty()){
            return ResultUtil.error(ResultEnum.PARAMS_ERROR, "用户名或密码不能为空");
        }
        if (loginAndRegisterService.login(user)){
            return ResultUtil.success("登录成功");
        }else {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, "登录失败");
        }
    }



}
