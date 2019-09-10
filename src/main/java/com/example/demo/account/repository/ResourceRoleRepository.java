package com.example.demo.account.repository;

import com.example.demo.account.entity.SysResourceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:28
 */
@Repository
public interface ResourceRoleRepository extends JpaRepository<SysResourceRole,Long> {
    List<SysResourceRole> findByResourceId(Long resourceId);
}
