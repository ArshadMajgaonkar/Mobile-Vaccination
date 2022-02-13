package com.aghs.mobilevaccination.config;

import com.aghs.mobilevaccination.data.model.AuthGroup;
import com.aghs.mobilevaccination.service.StaffUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class StaffSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    StaffUserDetailService staffUserDetailService;


    /**Initializes and configures Authentication Provider for the Admin and Staff role.*/
    @Bean
    public DaoAuthenticationProvider staffAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(staffUserDetailService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(11));
        provider.setAuthoritiesMapper(staffAuthoritiesMapper());
        return provider;
    }

    /**
     * Initializes and configures Authority Mapper for Authentication Provider
     * @return SimpleAuthorityMapper
     */
    @Bean
    public GrantedAuthoritiesMapper staffAuthoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setDefaultAuthority(AuthGroup.STAFF.toString());
        mapper.setConvertToUpperCase(true);
        return mapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(staffAuthenticationProvider());
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF");
        return roleHierarchy;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // redirects request starting with "/staff/" to form
                .antMatcher("/staff/**").authorizeRequests()
                // other request should have role ADMIN or STAFF
                // else forwarded to another configuration
                .anyRequest().hasAnyRole("ADMIN", "STAFF")
                // form based authentication
                .and()
                .formLogin()
                .loginPage("/staff/login").permitAll()
                .loginProcessingUrl("/staff/process-login")
                // if following lines are not added, it executes method partially.
                .successForwardUrl("/staff/process-login")
                .failureForwardUrl("/staff/process-login")
                // logout processing
                .and()
                .logout()
                .logoutUrl("/staff/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/logout-success");
    }
}
