package com.example.demo.account.service;

import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
public interface RoleService {
    SysRole findByName(String name);

    SysRole findById(Long roleId);
}
