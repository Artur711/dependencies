package com.java17.dependencies.annotations.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.java17.dependencies.annotations.util.ROLE.ROLE_ADMIN;
import static com.java17.dependencies.annotations.util.ROLE.ROLE_USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final static String ROOT_USER = "root-user";
    private final static String ROOT_PASSWORD = "root-password";
    private final static String SERVICE_USER = "user";
    private final static String SERVICE_PASSWORD = "password";
    private final static String ACTUATOR_PATH = "/app";

    @Value("${auth.root.user}")
    private String rootUser;

    @Value("${auth.root.password}")
    private String rootPassword;

    @Value("${auth.service.user}")
    private String serviceUser;

    @Value("${auth.service.password}")
    private String servicePassword;

    @Value("${management.endpoints.web.base-path}")
    private String actuatorPath;


    public SecurityConfig(CustomAuthEntryPoint customAuthEntryPoint) {
        this.customAuthEntryPoint = customAuthEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String healthPath = actuatorPath.isBlank() ? ACTUATOR_PATH : actuatorPath;
        http.csrf().disable()
                .cors().and()
                .authorizeRequests()
//                .antMatchers(actuatorPath + "/**", "/swagger-ui/**", "/v3/**")
                .antMatchers(healthPath + "/**")
                .hasAnyRole(ROLE_ADMIN.roleName())
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(customAuthEntryPoint);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(serviceUser.isBlank() ? SERVICE_USER : serviceUser)
                .password(passwordEncoder.encode(servicePassword.isBlank() ? SERVICE_PASSWORD : servicePassword))
                .roles(ROLE_USER.roleName())
                .build());
        manager.createUser(User.withUsername(rootUser.isBlank() ? ROOT_USER : rootUser)
                .password(passwordEncoder.encode(rootPassword.isBlank() ? ROOT_PASSWORD : rootPassword))
                .roles(ROLE_USER.roleName(), ROLE_ADMIN.roleName())
                .build());
        return manager;
    }
}
