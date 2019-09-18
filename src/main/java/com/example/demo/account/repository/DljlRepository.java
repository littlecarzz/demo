package com.example.demo.account.repository;

import com.example.demo.account.entity.Dljl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:28
 */
@Repository
public interface DljlRepository extends JpaRepository<Dljl,Long> {
    Dljl findByUsername(String username);
/*    加入：nativeQuery注解时，写原生sql，支持limit函数
    不加入：nativeQuery注解时是JPQL。JPQL不支持limit函数*/
    @Query(nativeQuery = true, value = "select d.dlsj from dljl d where d.username=?1 order by d.dlsj desc limit 1")
    String findLastTimeByUsername(String username);
}
