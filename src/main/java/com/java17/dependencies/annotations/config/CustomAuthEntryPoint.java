package com.java17.dependencies.annotations.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

//@Component
public class CustomAuthEntryPoint extends BasicAuthenticationEntryPoint {

//    @Override
//    public void commence(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException authException)
//            throws IOException {
//        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
//        response.setStatus(SC_UNAUTHORIZED);
//        final PrintWriter writer = response.getWriter();
//        writer.println(format("HTTP Status 401 - %s", authException.getMessage()));
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        setRealmName("JDeveloper");
//        super.afterPropertiesSet();
//    }
}
