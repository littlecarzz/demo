package com.example.demo.common.config;

import com.example.demo.account.entity.SysUser;
import org.apache.catalina.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/11/27 16:44
 */
//@Component
public class ServletRequestHandledEventListener implements ApplicationListener<ServletRequestHandledEvent> {
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private SessionManage sessionManage;

   private Logger log=LoggerFactory.getLogger(ServletRequestHandledEventListener.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        log.info("ServletRequestHandledEventListener:{}", event);
        String sessionId = event.getSessionId();
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInformation == null) {
            return;
        }
        Object principal = sessionInformation.getPrincipal();


        try {
            if (principal instanceof SysUser) {
                SysUser user = (SysUser) principal;
//                sessionManage.putUserSession(user.getId(), SecurityUtil.getHttpSession());
            }

        } catch (Exception e) {
            log.warn("当前无法监听到用户信息");
        }
    }
}
