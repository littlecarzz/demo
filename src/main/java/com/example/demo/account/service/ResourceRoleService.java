package com.example.demo.account.service;

import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysResourceRole;
import com.example.demo.account.entity.SysRole;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
public interface ResourceRoleService {
    List<SysResourceRole> findAll();
    List<SysRole> findRolesByResourceId(Long resourceId);


}
