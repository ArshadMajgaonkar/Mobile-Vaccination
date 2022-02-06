package com.aghs.mobilevaccination.config;

import com.aghs.mobilevaccination.data.model.AuthGroup;
import com.aghs.mobilevaccination.service.GeneralUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


/**
 * Security Configuration Class for General User Account
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private GeneralUserDetailService userDetailService;

    /**
     * Initializes Authentication Provider for General User Account
     * 1) Sets UserDetails serving object
     * 2) Sets Password Encoder to bcrypt
     * 3) Sets Authority Mapper
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider accountAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setAuthoritiesMapper(accountAuthoritiesMapper());
        return provider;
    }

    /**
     * Defines Authorities Mapper for General User Account
     * 1) Converts role names to upper case
     * 2) Sets Default authority to User
     * @return SimpleAuthorityMapper
     */
    @Bean
    public GrantedAuthoritiesMapper accountAuthoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setDefaultAuthority(AuthGroup.USER.toString());
        mapper.setConvertToUpperCase(true);
        return mapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(accountAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // redirects request starting with "/staff/" to form
                .antMatcher("/user/**").authorizeRequests()
                // other request should have role USER
                // else forwarded to another configuration
                .anyRequest().hasRole("USER")
                // form based authentication
                .and()
                .formLogin()
                .loginPage("/user/login")
                .usernameParameter("mobileNumber")
                .passwordParameter("otp").permitAll()
                .loginProcessingUrl("/user/process-login").permitAll()
                // if following lines are not added, it executes method partially.
                .successForwardUrl("/user/process-login")
                .failureForwardUrl("/user/process-login")
                //.successForwardUrl("/user/login")
                //.defaultSuccessUrl("/user/dashboard")
                // logout processing
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/logout-success");
    }
}

