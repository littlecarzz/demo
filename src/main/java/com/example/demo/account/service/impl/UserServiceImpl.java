package com.example.demo.account.service.impl;

import com.example.demo.account.entity.SysUser;
import com.example.demo.account.repository.UserRepository;
import com.example.demo.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public SysUser findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
    }
}
