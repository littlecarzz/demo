package com.example.demo.common.config;

import com.example.demo.common.error.MyAccessDeniedHandler;
import com.example.demo.common.exception.CaptchaException;
import com.example.demo.common.filter.LoginAuthenticationFilter;
import com.example.demo.common.service.CustomerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 *
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


    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customerDetailService);
        return daoAuthenticationProvider;
    }

    /**
     * 登录异常的处理
     * @return
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
     * 数据源
     * @return
     */
    @Bean(name = "mydatasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/demo");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    /**
     * 持久化token
     * Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中
     * 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource()); // 设置数据源
//        tokenRepository.setCreateTableOnStartup(true); // 启动创建表，创建成功后注释掉
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        loginAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        http
                .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/css/**","/js/**","/json/**","/layui/**","/images/**").permitAll()
                    .antMatchers("/login","/code","/loginValidateCode").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successForwardUrl("/index")
                    //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
//                  .successHandler(loginSuccessHandler())
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
                //无权限403的处理
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .rememberMe()
                    .userDetailsService(customerDetailService)
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(60*60*24*7)
                .and()
                .csrf().disable()
                ;
    }

/*    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/**");
    }*/

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