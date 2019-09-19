package com.example.demo.account.service.impl;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.repository.RoleRepository;
import com.example.demo.account.repository.UserRepository;
import com.example.demo.account.service.UserService;
import com.example.demo.common.service.CustomerDetailService;
import com.example.demo.common.utils.SpringSecurityUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerDetailService customerDetailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public SysUser findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(SysUser user) {
        userRepository.save(user);
    }

    @Override
    public void delete(SysUser user) {
        userRepository.delete(user);
    }

    @Override
    public List<SysUser> findByUsernameLike(String username) {
        return userRepository.findByUsernameLike(username);
    }

    /**
     * 确认用户角色选择后进行登录
     * @param roleId
     * @param request
     * @param currentUserDetails
     */
    @Override
    public void doRoleChoose(Long roleId, HttpServletRequest request, SecurityUser currentUserDetails) {
        Optional<SysRole> role = roleRepository.findById(roleId);
        Collection<GrantedAuthority> authorities = new LinkedHashSet<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.get().getName());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentUserDetails.getUsername()+"@@@"+roleId.toString(), currentUserDetails.getUserPwd(), authorities);
        try{
            token.setDetails(new WebAuthenticationDetails(request));
            DaoAuthenticationProvider authenticator = new DaoAuthenticationProvider();
            authenticator.setUserDetailsService(customerDetailService);
            authenticator.setPasswordEncoder(passwordEncoder);
            Authentication authentication = authenticator.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,SecurityContextHolder.getContext());

        }catch (AuthenticationException e){
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }


}
