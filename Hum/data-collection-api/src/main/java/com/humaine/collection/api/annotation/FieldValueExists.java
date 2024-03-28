package com.humaine.collection.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.data.repository.CrudRepository;

import com.humaine.collection.api.validators.ValueExistsValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValueExistsValidator.class)
public @interface FieldValueExists {
	String requiredMsg() default "";
	
	String requiredMsgErorCode() default "";

	String message();
	
	String messageCode();

	Class<? extends CrudRepository> repository();

	String repositoryQualifier() default "";

	boolean required() default true;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
