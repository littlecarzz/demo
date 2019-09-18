package com.example.demo.common.utils;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/18 15:38
 */
public class SpringSecurityUtils {
    private static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) {
            return null;
        }

        return context.getAuthentication();
    }
    public static <T extends SysUser> T getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SysUser)) {
            return null;
        }
        return (T) principal;
    }
    public static String getCurrentUserName() {
        Authentication authentication = getAuthentication();

        if ((authentication == null) || (authentication.getPrincipal() == null)) {
            return "";
        }
        return authentication.getName();
    }
    public static SecurityUser getCurrentUserDetails() {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails;
    }
    public static Long getCurrentUserAuthId() {
        SecurityUser userDetails = getCurrentUserDetails();
        return userDetails.getCurrUserRoleId();
    }
}
