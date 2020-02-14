package stoner.tshiro.service;

import stoner.tshiro.bean.User;

public interface LoginService {

    User getUser();

    User getUser(String name);
}
