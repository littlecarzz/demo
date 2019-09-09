package com.example.demo.account.service.impl;

import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysResourceRole;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.repository.ResourceRepository;
import com.example.demo.account.repository.ResourceRoleRepository;
import com.example.demo.account.repository.RoleRepository;
import com.example.demo.account.service.ResourceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:30
 */
@Service
public class ResourceRoleServiceImpl  implements ResourceRoleService {
    @Autowired
    private ResourceRoleRepository resourceRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<SysResourceRole> findAll() {
        return resourceRoleRepository.findAll();
    }

    @Override
    public List<SysRole> findRolesByResourceId(String resourceId) {
        List<SysResourceRole> resourceRoleList=resourceRoleRepository.findByResourceId(resourceId);
        if(resourceRoleList==null || resourceRoleList.size()==0){
            return null;
        }
        List<SysRole> roleList = new LinkedList<>();
        for (SysResourceRole resourceRole :
                resourceRoleList) {
            Optional<SysRole> role = roleRepository.findById(resourceRole.getRoleId());
            roleList.add(role.get());
        }
        return roleList;
    }
}
