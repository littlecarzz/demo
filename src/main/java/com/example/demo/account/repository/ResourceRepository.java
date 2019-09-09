package com.example.demo.account.repository;

import com.example.demo.account.entity.SysResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:28
 */
@Repository
public interface ResourceRepository extends JpaRepository<SysResource,Long> {
}
