package com.example.demo.account.service.impl;

import com.example.demo.account.entity.SysUser;
import com.example.demo.account.repository.UserRepository;
import com.example.demo.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public SysUser findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }
}
