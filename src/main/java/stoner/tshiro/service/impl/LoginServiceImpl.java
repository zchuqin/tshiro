package stoner.tshiro.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import stoner.tshiro.bean.Permission;
import stoner.tshiro.bean.Role;
import stoner.tshiro.bean.User;
import stoner.tshiro.service.LoginService;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static stoner.tshiro.common.CommonConstants.*;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SessionManager defaultWebSessionManager;

    @Autowired
    private EhCacheManager ehCacheManager;

    private static Map<String, User> userMap;

    static {
        Permission permission = new Permission("1", "*");
        Permission permission1 = new Permission("2", "add");
        Permission permission2 = new Permission("3", "query");
        HashSet<Permission> permissions = new HashSet<>();
        permissions.add(permission);
        permissions.add(permission1);
        Role role = new Role("a", "admin", permissions);
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        String userName = "peter";
        User peter = new User("1", userName, "456", roles);

        HashSet<Permission> permissions1 = new HashSet<>();
        permissions1.add(permission2);
        Role role1 = new Role("u", "user", permissions1);
        HashSet<Role> roles1 = new HashSet<>();
        roles1.add(role1);
        String userName1 = "May";
        User may = new User("2", userName1, "789", roles1);

        userMap = new HashMap<>();
        userMap.put(userName, peter);
        userMap.put(userName1, may);
    }

    @Override
    public User getUser() {
        Collection<User> values;
        if (userMap == null || CollectionUtils.isEmpty((values = userMap.values()))) {
            return null;
        }
        return values.iterator().next();
    }

    @Override
    public User getUser(String name) {
        if (userMap == null) {
            return null;
        }
        return userMap.get(name);
    }

    @Override
    public String login(AuthenticationToken authenticationToken) {
        if (!(authenticationToken instanceof UsernamePasswordToken)) {
            return null;
        }
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
//        if (principal != null && principal.toString().equalsIgnoreCase(userName)) {
//            log.info("用户 {} 已登录", userName);
//            return "您已登录";
//        } else {
            User user = getUser(userName);
            Object credentials;
            if (user == null) {
                if (principal != null) {
                    return "用户名错误；您已登录";
                }
                throw new UnknownAccountException(userName);
            } else if ((credentials = token.getCredentials()) instanceof char[] && (new String((char[]) credentials)).equals(user.getPassword())) {
                log.info("用户 {} 登录成功", userName);
                kickoutSessions(userName);
                subject.login(token);
                return "登录成功";
            } else {
                if (principal != null) {
                    return "密码错误；您已登录";
                }
                throw new IncorrectCredentialsException(userName);
            }
//        }
    }

    @Override
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            log.info("未登录，不能注销");
            return "请您先登录";
        } else {
            log.info("用户 {} 已注销", principal.toString());
            subject.logout();
            return "已成功注销";
        }
    }

    private void kickoutSessions(String userName) {
        Serializable id = getCurrentSession().getId();
        for (Session session : getActiveSessions()) {
            if (String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).equalsIgnoreCase(userName) && !session.getId().equals(id)) {
                //把之前登录的同一个用户下的会话踢出
                session.setAttribute(KICKOUT_KEY, Boolean.TRUE);
            }
        }
    }

    public Boolean checkSessionKickout() {
        return (Boolean)getCurrentSession().getAttribute(KICKOUT_KEY);
    }

    private Collection<Session> getActiveSessions() {
        DefaultWebSessionManager defaultWebSessionManager = (DefaultWebSessionManager) this.defaultWebSessionManager;
        SessionDAO sessionDAO = defaultWebSessionManager.getSessionDAO();
        return sessionDAO.getActiveSessions();
    }

    private Session getCurrentSession() {
        return SecurityUtils.getSubject().getSession();
    }
}
