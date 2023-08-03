package info.phj233.onlinechat.evaluator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义权限注解验证类
 * @author phj233
 * @since  2023/3/12 22:15
 * @version 1.0
 * @deprecated Security升级6.0后已失效
 */
@Slf4j
@Deprecated
public class SelfPermissionEvaluatorImpl implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        log.info("【权限验证】 {} 权限{}" ,authentication.getAuthorities(), permission);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> permissions = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return permissions.contains(permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
