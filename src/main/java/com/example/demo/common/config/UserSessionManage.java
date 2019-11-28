package com.example.demo.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 用户存储session
 * 一个用户储存多个session 出现一个用户多个地方登录
 * @author littlecar
 * @date 2019/11/27 16:26
 */
@Component
@Data
@Slf4j
@CacheConfig(cacheNames = "UserSessionManage")
public class UserSessionManage {
    /**
     * 用户存储session对象
     */
    private Map<String, List<HttpSession>> userHttpSession=new HashMap<>();
    /**
     * 获取当前在线的session
     * @param id
     * @return
     */
    @Cacheable(key = "#id")
    public List<HttpSession> getUserSession(String id) {
        List <HttpSession> httpSessions = userHttpSession.get(id);
        if(httpSessions==null){
            httpSessions=new ArrayList<>();
        }
        return httpSessions;
    }

    /**
     * 修改缓存
     * @param id
     * @param userSession
     */
    @CachePut(key = "#id")
    public List<HttpSession> putUserSession(String id, List<HttpSession> userSession) {
        userHttpSession.put(id,userSession);
        return userSession;
    }
}
