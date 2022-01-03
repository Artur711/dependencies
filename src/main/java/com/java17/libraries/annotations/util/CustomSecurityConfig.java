package com.java17.libraries.annotations.util;

import com.java17.libraries.annotations.config.SecurityConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SecurityConfig.class})
public @interface CustomSecurityConfig {
}
