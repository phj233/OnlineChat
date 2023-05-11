package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.Result;
import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.controller
 * @className: AdminController
 * @author: phj233
 * @date: 2023/3/12 22:29
 * @version: 1.0
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserDao userDao;
    private final UserService userService;
    @PreAuthorize("hasAuthority('ROLE_admin')")
    @GetMapping("/list")
    public Result<Object> list() {
        return ResultUtil.success(userDao.findAll());
    }

    @PreAuthorize("hasPermission('/admin/addUser', 'admin')")
    @PostMapping("/addUser")
    public Result<Object> addUser(@RequestBody User user) {
        try {
            if ( userService.addUser(user) ) {
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error(ResultEnum.OPERATION_ERROR);
            }
        }catch (Exception e) {
            return ResultUtil.error(ResultEnum.OPERATION_ERROR, e.getMessage());
        }
    }

}
