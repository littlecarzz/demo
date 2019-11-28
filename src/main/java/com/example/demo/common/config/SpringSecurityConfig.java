package com.example.demo.common.config;

import com.example.demo.account.entity.SysUser;
import com.example.demo.common.exception.CaptchaException;
import com.example.demo.common.filter.LoginAuthenticationFilter;
import com.example.demo.common.filter.MySecurityFilter;
import com.example.demo.common.service.CustomerDetailService;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：spring security 配置
 * @author littlecar
 * @date 2019/9/4 11:13
 */
@Configuration
//启用方法级别安全隔离
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private CustomerDetailService customerDetailService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MySecurityFilter mySecurityFilter;

    /**
     * 注册bean sessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 密码加密
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录成功后的处理
     * @return LoginSuccessHandler
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    /**
     * 自定义登录验证
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customerDetailService);
        return daoAuthenticationProvider;
    }

    /**
     * 登录异常的处理
     * @return AuthenticationFailureHandler
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        ExceptionMappingAuthenticationFailureHandler failureHandler = new ExceptionMappingAuthenticationFailureHandler();
        Map<String, String> failureUrlMap = new HashMap<>();
        failureUrlMap.put(BadCredentialsException.class.getName(), LoginAuthenticationFailureHandler.PASS_ERROR_URL);
        failureUrlMap.put(CaptchaException.class.getName(), LoginAuthenticationFailureHandler.CODE_ERROR_URL);
        failureUrlMap.put(AccountExpiredException.class.getName(), LoginAuthenticationFailureHandler.EXPIRED_URL);
        failureUrlMap.put(LockedException.class.getName(), LoginAuthenticationFailureHandler.LOCKED_URL);
        failureUrlMap.put(DisabledException.class.getName(), LoginAuthenticationFailureHandler.DISABLED_URL);
        failureHandler.setExceptionMappings(failureUrlMap);
        return failureHandler;
    }

    /**
     * 自定义RememberMeServices
     * @return PersistentTokenBasedRememberMeServices
     * @throws SQLException
     */
    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() throws SQLException {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices("remember-me", customerDetailService, persistentTokenRepository());
        rememberMeServices.setTokenValiditySeconds(60*60*24*7);
        return rememberMeServices;
    }

    /**
     * 自定义登录过滤器
     * {@link UsernamePasswordAuthenticationFilter}
     * @return LoginAuthenticationFilter
     * @throws Exception
     */
    public LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        //登录失败
        loginAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        //登录成功后可使用loginSuccessHandler()存储查询用户信息，主页跳转等，可选。
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        //RememberMeServices
        loginAuthenticationFilter.setRememberMeServices(persistentTokenBasedRememberMeServices());
        return loginAuthenticationFilter;
    }

    /**
     * 持久化token
     * Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中
     * 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库
     * @return PersistentTokenRepository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() throws SQLException {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        // 启动创建表，创建成功后注释掉
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(mySecurityFilter, FilterSecurityInterceptor.class)
                //无权限403的处理
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .authorizeRequests()
                    .antMatchers("/login","/code","/loginValidateCode","/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
//                    successFordwardUrl只支持post请求，如果你得首页是get请求，你可以使用successHandler 然后做个重定向
//                    successForwardUrl需要配合自定义的AuthenticationSuccessHandler使用，
//                    defaultSuccessUrl相当于有一个AuthenticationSuccessHandler默认的实现类SavedRequestAwareAuthenticationSuccessHandler
//                    .defaultSuccessUrl("/test",true)
//                    .successForwardUrl("/test")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    //清除身份认证信息
                    .clearAuthentication(true)
                    //使session失效
                    .invalidateHttpSession(true)
                    .permitAll()
                .and()
                .rememberMe()
//                    .userDetailsService(customerDetailService)
//                    .tokenRepository(persistentTokenRepository())
//                    .tokenValiditySeconds(60*2)
                .and()
                .headers()
//                  X-Frame-Options HTTP 响应头
                    .frameOptions().sameOrigin()
                .and()
                .sessionManagement()
                    //session无效的跳转
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    //session过期的跳转
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry())
                .and()

                .and()
                .csrf()
                    .disable()
                ;
    }

    /**
     * 解决静态资源被拦截的问题
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/json/**","/layui/**","/images/**",
                //swagger api json
                "/v2/api-docs",
                //用来获取支持的动作
                "/swagger-resources/configuration/ui",
                //用来获取api-docs的URI
                "/swagger-resources",
                //安全选项
                "/swagger-resources/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    /**
     *create two users admin and user
     * 测试专用,不需要经过数据库
     * @param auth
     * @throws Exception
     * */
/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder()).withUser("user").password(new BCryptPasswordEncoder().encode("123456")).roles("user")
                .and()
                .passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("admin");
    }*/

}
