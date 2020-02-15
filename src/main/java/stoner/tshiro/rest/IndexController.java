package stoner.tshiro.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @ResponseBody
    @RequestMapping("/error/{status}")
    public String errorPage(@PathVariable String status) {
        return "<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head><body><div style=\"font-size:10em;\">" + status + "</div></body></html>";
    }

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        System.out.println(SecurityUtils.getSubject().isRemembered());
        System.out.println(SecurityUtils.getSubject().isAuthenticated());
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return "<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head><body><div style=\"font-size:10em;\">" + principal + "</div></body></html>";
    }

    @RequestMapping("/index1")
    @ResponseBody
    public String index1() {
        System.out.println(SecurityUtils.getSubject().isRemembered());
        System.out.println(SecurityUtils.getSubject().isAuthenticated());
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return "<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head><body><div style=\"font-size:10em;\">index1  " + principal + "</div></body></html>";
    }
}
