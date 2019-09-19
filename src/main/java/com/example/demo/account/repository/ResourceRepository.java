package com.example.demo.account.repository;

import com.example.demo.account.entity.SysResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:28
 */
@Repository
public interface ResourceRepository extends JpaRepository<SysResource,Long> {
    @Query(value = "from  SysResource s where s.id in(select a.resourceId from SysResourceRole a where a.roleId=?1 ) order by s.num")
    List<SysResource> findResourceByRoleId(Long userRoleId);
}
