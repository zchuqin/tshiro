package stoner.tshiro.service;

import org.apache.shiro.authc.AuthenticationToken;
import stoner.tshiro.bean.User;

public interface LoginService {

    User getUser();

    User getUser(String name);

    String login(AuthenticationToken token);

    String logout();
}
