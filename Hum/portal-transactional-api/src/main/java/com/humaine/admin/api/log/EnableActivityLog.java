package com.humaine.admin.api.log;

import org.springframework.context.annotation.Import;

import com.humaine.admin.api.log.config.ActivityLogConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ActivityLogConfig.class)
public @interface EnableActivityLog {
}