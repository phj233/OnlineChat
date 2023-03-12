package info.phj233.onlinechat.controller;

import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.util.Result;
import info.phj233.onlinechat.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/list")
    public Result<Object> list() {
        return ResultUtil.success(userDao.findUserByRole("admin"));
    }
}
