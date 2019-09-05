package com.example.demo.account.repository;

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
public interface UserRepository extends JpaRepository<SysUser,Long> {
    /**
     * 根据name查找SysUser
     * @param name
     * @return SysUser
     */
    SysUser findByName(String name);
}
