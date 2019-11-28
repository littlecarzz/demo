package com.example.demo.common.config;

import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.service.impl.ResourceRoleServiceImpl;
import com.example.demo.account.service.impl.ResourceServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 描述：系统启动时将资源和权限的对应信息关联起来
 * @author littlecar
 * @date 2019/9/9 15:20
 */
@Service
public class CustomInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ResourceServiceImpl resourceService;
    @Autowired
    private ResourceRoleServiceImpl resourceRoleService;

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     * 被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。
     * PostConstruct在构造函数之后执行,init()方法之前执行。
     * 一定要加上@PostConstruct注解
     */
    @PostConstruct
    private void loadResourceDefine() {
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        // 在Web服务器启动时，提取系统中的所有权限。
        List<SysResource> resources = resourceService.findAll();
        for (SysResource resource :
                resources) {
            List<SysRole> roles = resourceRoleService.findRolesByResourceId(resource.getId());
            String url = resource.getUrl();
            Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
            for (SysRole role :
                    roles) {
                ConfigAttribute ca = new SecurityConfig(role.getName());
                atts.add(ca);
            }
            resourceMap.put(url, atts);
        }
    }

    /**
     * 根据url找到相关权限配置
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // object 是一个URL，被用户请求的url。
        FilterInvocation filterInvocation = (FilterInvocation) object;
        if (resourceMap == null) {
            loadResourceDefine();
        }
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
            if(requestMatcher.matches(filterInvocation.getHttpRequest())) {
                return resourceMap.get(resURL);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<ConfigAttribute>();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
