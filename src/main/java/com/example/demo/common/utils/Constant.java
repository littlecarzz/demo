package com.example.demo.common.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：常用参数定义
 * @author littlecar
 * @date 2019/9/12 16:53
 */
public class Constant {

    public static final String ADMIN = "管理员";
    public static final String USER = "普通用户";
    public static final Map<String, String> roleMap = new HashMap<>();
    static{
        roleMap.put("ADMIN", "管理员");
        roleMap.put("USER", "普通用户");
    }
    public static final Map<Long, String> roleIdMap = new HashMap<>();
    static{
        roleIdMap.put(1L, "ADMIN");
        roleIdMap.put(2L, "USER");
    }

    public static final String initialPassword = "123456";

}
