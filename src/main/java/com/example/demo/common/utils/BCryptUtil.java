package com.example.demo.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 描述:{@link BCryptPasswordEncoder}
 * @author littlecar
 * @create 2019-09-05 22:25
 */
public class BCryptUtil {

    /**
     * 对密码进行加密
     * @param password
     * @return
     */
    public static String encode(String password) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(password);
        return hashPass;
    }
    /**
     * 对原密码和已加密的密码进行匹配，判断是否相等
     * @param password
     * @param encodedPassword
     * @return
     */
    public static boolean match(String password,String encodedPassword) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean result = bcryptPasswordEncoder.matches(password, encodedPassword);
        return result;
    }
    public static void main(String[] args) {
        String hashPass = encode("123456");
        System.out.println(hashPass);
        System.out.println(match("123456",hashPass));//true
        System.out.println(match("123456","$2a$10$7wOQPHU2MfHt3X4wCFx5H.EZu.rlHMtY5HTFsqXiPd6BA5vNHJNf2"));//true
        System.out.println(match("123456","$2a$10$nYQWXcY.eVUwI8kYGtMCVOD0hWE4AKjzFg0oo91qc/ECQg/DD/CpS"));//true
        System.out.println(match("123456","$2a$10$9etIPtquQ3f..ACQkDHAVuBfjBoDXXWHHCOBl/RaJADxuXdSQB6I2"));//true
    }


}
