package com.java17.dependencies.annotations.config;

import com.java17.dependencies.annotations.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthEntryPoint customAuthEntryPoint;

    @Value("${auth.root.user}")
    private String rootUser;

    @Value("${auth.root.password}")
    private String rootPassword;

    @Value("${auth.service.user}")
    private String serviceUser;

    @Value("${auth.service.password}")
    private String servicePassword;


    public SecurityConfig(CustomAuthEntryPoint customAuthEntryPoint) {
        this.customAuthEntryPoint = customAuthEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(customAuthEntryPoint);
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        setUserInAuthMemory(auth, new AppUser(rootUser, rootPassword,"ROLE_ADMIN"));
        setUserInAuthMemory(auth, new AppUser(serviceUser, servicePassword,"ROLE_ADMIN"));
    }

    private void setUserInAuthMemory(AuthenticationManagerBuilder auth, AppUser appUser) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser(appUser.user())
                .password(passwordEncoder.encode(appUser.password()))
                .roles(appUser.role());
    }
}
