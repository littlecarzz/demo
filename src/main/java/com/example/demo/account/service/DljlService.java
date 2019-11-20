package com.example.demo.account.service;

import com.example.demo.account.entity.Dljl;
import com.example.demo.account.entity.SysResourceRole;
import com.example.demo.account.entity.SysRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
public interface DljlService {

    Dljl findByUsername(String username);

    void save(Dljl dljl);

    String findLastTimeByUsername(String username);
}
