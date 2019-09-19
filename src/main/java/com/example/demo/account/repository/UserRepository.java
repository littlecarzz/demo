package com.example.demo.account.repository;

import com.example.demo.account.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:23
 */
@Repository
public interface UserRepository extends JpaRepository<SysUser,Long> {
    /**
     * 根据username查找SysUser
     * @param username
     * @return SysUser
     */
    SysUser findByUsername(String username);

    List<SysUser> findByUsernameLike(String username);

}
