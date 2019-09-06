package com.example.demo.common.config;

import com.example.demo.common.error.MyAccessDeniedHandler;
import com.example.demo.common.service.CustomerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**","/js/**","/json/**","/layui/**","/images/**",
                        "/login","/code","/loginValidateCode").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureForwardUrl("/login-error")
                .successForwardUrl("/index")
                //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
//                .successHandler(loginSuccessHandler())
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                //清除身份认证信息
                .clearAuthentication(true)
                //使session无效
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                //无权限403的处理
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)
//                .and()
////                .rememberMe()
////                .and()
//                .csrf().disable()
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