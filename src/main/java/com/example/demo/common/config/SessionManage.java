package com.example.demo.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：session管理容器
 *
 * @author littlecar
 * @date 2019/11/27 16:28
 */
@Data
@Slf4j
@Component
@CacheConfig(cacheNames = "SessionManage")
public class SessionManage {
    @Autowired
    UserSessionManage userSessionManage;
    /**
     * 创建map容器
     */
    private Map<String,HttpSession> httpSessionMap=new HashMap<>();

    /**
     * 创建session
     * @param id
     * @param session
     */
    @Cacheable(key = "#id")
    public HttpSession createdSession(String id, HttpSession session) {
        httpSessionMap.put(id,session);
        return session;
    }

    /**
     * 销毁session
     * @param id
     */
    @CacheEvict(key = "#id")
    public void destroyedSession(String id) {
        httpSessionMap.remove(id);
    }

    /**
     * 改变session
     * @param oldSessionId
     * @param newSessionId
     * @return
     */
    public void changeSession(String oldSessionId, String newSessionId) {
        //查询旧的session

    }
    /**
     * 根据sessionid查询session
     */
    @Cacheable(key = "#id")
    public HttpSession getSessionID(String id){
        return httpSessionMap.get(id);
    }

    /**
     * 当前用户保存session
     * @param id 用户id
     * @param httpSessions
     */
    @Cacheable(key = "'user'+#id",unless = "!#result.contains(#httpSessions)")
    public List<HttpSession> putUserSession(String id, HttpSession httpSessions) {
        List <HttpSession> userSession = userSessionManage.getUserSession(id);
        if(!userSession.contains(httpSessions)){
            userSession.add(httpSessions);
        }
        userSessionManage.putUserSession(id,userSession);
        return userSession;
    }
}
