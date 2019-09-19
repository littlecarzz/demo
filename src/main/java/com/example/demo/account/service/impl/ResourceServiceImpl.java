package com.example.demo.account.service.impl;

import com.example.demo.account.entity.ResourceInfo;
import com.example.demo.account.entity.SysResource;
import com.example.demo.account.repository.ResourceRepository;
import com.example.demo.account.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<SysResource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public List<SysResource> findResourceByRoleId(Long userRoleId) {
        return resourceRepository.findResourceByRoleId(userRoleId);
    }

    @Override
    public List<ResourceInfo> combineJson(List<SysResource> resourceList) {
        List<ResourceInfo> resourceInfos = new ArrayList<>();
        for (SysResource resource :
                resourceList) {
            if (resource.getParent()==null){
                ResourceInfo resourceInfo = new ResourceInfo();
                resourceInfo.setTitle(resource.getTitle());
                resourceInfo.setHref(resource.getUrl());
                resourceInfo.setIcon(resource.getIcon());
                resourceInfo.setSpread(false);
                List<SysResource> subResources = resource.getSubResources();
                subResources.sort((a, b) -> a.getNum() - b.getNum());
                List<ResourceInfo> chlidren = new ArrayList<>();
                if (subResources.size()>0){
                    for (SysResource resource1 :
                            subResources) {
//                        if (resourceList.contains(resource1)) {
                        ResourceInfo resourceInfo1 = new ResourceInfo();
                        resourceInfo1.setTitle(resource1.getTitle());
                        resourceInfo1.setHref(resource1.getUrl());
                        resourceInfo1.setIcon(resource1.getIcon());
                        resourceInfo1.setSpread(false);
                        chlidren.add(resourceInfo1);
//                        }
                    }
                }
                resourceInfo.setChildren(chlidren);
                resourceInfos.add(resourceInfo);
            }
        }
        return resourceInfos;
    }
}
