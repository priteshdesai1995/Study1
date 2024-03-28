package com.base.api.annotations;

import java.lang.reflect.Method;
import java.util.Collection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.base.api.exception.APIException;

@Aspect
@Component
public class SecuredWIthPermissionAspect {

	@Around("methodsAnnotatedWithSecuredWIthPermissionAnnotation()")
	public Object processMethodsAnnotatedWithProjectSecuredAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SecuredWIthPermission securedWIthPermissionAnnotation = method.getAnnotation(SecuredWIthPermission.class);
		String[] permissionStrings = securedWIthPermissionAnnotation.permissions();
		System.out.print(permissionStrings);
		if (permissionStrings == null || (permissionStrings != null && permissionStrings.length == 0)) {
			throw new APIException("api.error.method.security.unauthorized", HttpStatus.UNAUTHORIZED);
		}

		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			throw new APIException("api.error.method.security.unauthorized", HttpStatus.UNAUTHORIZED);
		}

		Authentication authentication = context.getAuthentication();
		System.out.print(authentication);
		if (authentication == null) {
			throw new APIException("api.error.method.security.unauthorized", HttpStatus.UNAUTHORIZED);
		}

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean authorized = false;
		System.out.print(permissionStrings);
		for (String permission : permissionStrings) {
			if (!authorized && authorities.contains(new SimpleGrantedAuthority(permission))) {
				authorized = true;
			}
		}

		if (authorized) {
			return joinPoint.proceed();
		}

		throw new APIException("api.error.method.security.unauthorized", HttpStatus.UNAUTHORIZED);
	}

	@Pointcut("@annotation(com.base.api.annotations.SecuredWIthPermission)")
	private void methodsAnnotatedWithSecuredWIthPermissionAnnotation() {

	}
}
