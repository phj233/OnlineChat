package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.repository.UserRepository;
import info.phj233.onlinechat.model.User;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.util.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员接口
 * @author phj233
 * @since  2023/3/12 22:29
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/list")
    public R<List<User>> list() {
        return R.ok(userRepository.findAll());
    }

    @PreAuthorize("hasPermission('/admin/addUser', 'admin')")
    @PostMapping("/addUser")
    public R<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

}
