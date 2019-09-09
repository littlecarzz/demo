package com.example.demo.account.service.impl;

import com.example.demo.account.entity.SysResource;
import com.example.demo.account.repository.ResourceRepository;
import com.example.demo.account.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/9 15:30
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<SysResource> findAll() {
        return resourceRepository.findAll();
    }
}
