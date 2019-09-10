package com.example.demo.common.config;

import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.repository.ResourceRepository;
import com.example.demo.account.repository.ResourceRoleRepository;
import com.example.demo.account.service.impl.ResourceRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/10 17:12
 */
//@Component
public class ResourcesCacheManager {
    public static final String ACEGI_EHCACHE = "resourceCache";
    boolean cacheInitialized = false;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceRoleServiceImpl resourceRoleService;

    /**
     * 初始化resourceCache
     */
    public void initResourceCache(){
        Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        List<SysResource> resourceList = resourceRepository.findAll();
        for (SysResource resource:
             resourceList) {
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
}
