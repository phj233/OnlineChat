package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/list")
    public R<List<User>> list() {
        return R.ok(userDao.findAll());
    }

    @PreAuthorize("hasPermission('/admin/addUser', 'admin')")
    @PostMapping("/addUser")
    public R<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

}
