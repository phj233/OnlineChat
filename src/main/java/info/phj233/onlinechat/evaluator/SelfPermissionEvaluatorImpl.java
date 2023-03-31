package info.phj233.onlinechat.evaluator;

import info.phj233.onlinechat.dao.UserDao;
import info.phj233.onlinechat.model.User;
import jakarta.annotation.Resource;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * 自定义权限注解验证类
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.evaluator
 * @className: SelfPermissionEvaluatorImpl
 * @author: phj233
 * @date: 2023/3/12 22:15
 * @version: 1.0
 */
@Component
public class SelfPermissionEvaluatorImpl implements PermissionEvaluator {
    @Resource
    private UserDao userDao;
    /**
     * 判断用户是否有权限
     * 这里仅仅判断 PreAuthorize 注解中的权限表达式
     * 实际中可以根据业务需求设计数据库通过 targetUrl 和 permission 做更复杂鉴权
     * 当然 targetUrl 不一定是URL可以是数据Id还可以是管理员标识等,这里根据需求自行设计
     * @use Mapping上注解@PreAuthorize("hasPermission(/admin/userList, 'admin')")
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // 获取用户信息
        User user = (User) authentication.getPrincipal();
        // 获取用户权限(这里可以将权限放入redis中,这里为了方便直接从数据库中获取)
        Set<String> permissions = Set.of(userDao.findUserByUsername(user.getUsername()).getRole().split(","));
        // 判断用户是否有权限
        return permissions.contains(permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
