package stoner.tshiro.rest;

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

    //注解验角色和权限
    @RequiresPermissions("add")
    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head><body><div style=\"font-size:10em;\">index</div></body></html>";
    }
}
