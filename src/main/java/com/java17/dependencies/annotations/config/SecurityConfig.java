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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(actuatorPath + "/**", "/swagger-ui/**", "/v3/**")
                .hasAnyRole("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(serviceUser)
                .password(passwordEncoder.encode(servicePassword))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername(rootUser)
                .password(passwordEncoder.encode(rootPassword))
                .roles("USER", "ADMIN")
                .build());
        return manager;
    }
}
