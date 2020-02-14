package stoner.tshiro.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import stoner.tshiro.bean.Permission;
import stoner.tshiro.bean.Role;
import stoner.tshiro.bean.User;
import stoner.tshiro.service.LoginService;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    private static Map<String, User> userMap;

    static {
        Permission permission = new Permission("1","*");
        Permission permission1 = new Permission("2","add");
        Permission permission2 = new Permission("3","query");
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
}
