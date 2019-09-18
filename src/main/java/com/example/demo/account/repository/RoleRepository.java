package com.example.demo.account.repository;

import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:23
 */
@Repository
public interface RoleRepository extends JpaRepository<SysRole,Long> {
    SysRole findByName(String name);
}
