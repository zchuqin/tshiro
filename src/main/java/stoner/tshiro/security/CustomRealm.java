package stoner.tshiro.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import stoner.tshiro.bean.Permission;
import stoner.tshiro.bean.Role;
import stoner.tshiro.bean.User;
import stoner.tshiro.service.LoginService;
import stoner.tshiro.utils.ApplicationContextUtil;

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    private static LoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String name = (String) principalCollection.getPrimaryPrincipal();
        //根据用户名去数据库查询用户信息
        User user = getLoginService().getUser(name);
        if (user == null) {
            return null;
        }
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
            //添加权限
            for (Permission permissions : role.getPermissions()) {
                simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            //这里返回后会报出对应异常 UnknownAccountException
            return null;
        }
        String name = authenticationToken.getPrincipal().toString();
        ByteSource salt = ByteSource.Util.bytes(name);
        return new SimpleAuthenticationInfo(name, authenticationToken.getCredentials(), salt, getName());
    }

    private static synchronized LoginService getLoginService() {
        if (loginService == null) {
            loginService = ApplicationContextUtil.getBean(LoginService.class);
        }
        return loginService;
    }

}
