package com.example.demo.account.service.impl;

import com.example.demo.account.entity.Dljl;
import com.example.demo.account.entity.SysResourceRole;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.repository.DljlRepository;
import com.example.demo.account.repository.ResourceRoleRepository;
import com.example.demo.account.repository.RoleRepository;
import com.example.demo.account.service.DljlService;
import com.example.demo.account.service.ResourceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DljlServiceImpl implements DljlService {
    @Autowired
    private DljlRepository dljlRepository;

    @Override
    public Dljl findByUsername(String username) {
        return dljlRepository.findByUsername(username);
    }

    @Override
    public void save(Dljl dljl) {
        dljlRepository.save(dljl);
    }

    @Override
    public String findLastTimeByUsername(String username) {
        return dljlRepository.findLastTimeByUsername(username);
    }
}
