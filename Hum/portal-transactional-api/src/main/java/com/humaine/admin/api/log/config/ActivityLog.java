package com.humaine.admin.api.log.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityLog {

    @AliasFor("template")
    String value() default "";
    
    @AliasFor("value")
    String template() default "";
    
    boolean onPre() default false;
}
