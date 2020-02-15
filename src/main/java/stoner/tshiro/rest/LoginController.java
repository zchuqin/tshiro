package stoner.tshiro.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import stoner.tshiro.bean.User;
import stoner.tshiro.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(User user, HttpServletRequest request) {
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(user.getUserName(), user.getPassword(), true, request.getRemoteHost());
        return loginService.login(usernamePasswordToken);
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public String logout() {
        return loginService.logout();
    }

}
