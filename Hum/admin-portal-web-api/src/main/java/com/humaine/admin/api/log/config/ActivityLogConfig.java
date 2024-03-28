package com.humaine.admin.api.log.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@Import(ActivityLogAspect.class)
public class ActivityLogConfig {
}
