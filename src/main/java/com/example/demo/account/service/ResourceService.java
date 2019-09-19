package com.example.demo.account.service;

import com.example.demo.account.entity.ResourceInfo;
import com.example.demo.account.entity.SysResource;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
public interface ResourceService {
    List<SysResource> findAll();
    List<SysResource> findResourceByRoleId(Long userRoleId);

    List<ResourceInfo> combineJson(List<SysResource> resourceList);
}
