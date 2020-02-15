package stoner.tshiro.security;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Marker;

@Slf4j
public class ShiroSessionListener implements SessionListener {
    @Override
    public void onStart(Session session) {
        log.info(JSON.toJSONString(session));
    }

    @Override
    public void onStop(Session session) {

    }

    @Override
    public void onExpiration(Session session) {

    }
}
