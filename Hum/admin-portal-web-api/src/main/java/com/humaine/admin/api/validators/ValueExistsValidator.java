package com.humaine.admin.api.validators;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.humaine.admin.api.annotation.FieldValueExists;
import com.humaine.admin.api.util.ErrorMessageUtils;

@Component
public class ValueExistsValidator implements ConstraintValidator<FieldValueExists, Object> {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private CrudRepository<Object, Object> repository;

	private boolean required;

	String requiredMsg;

	String requiredMsgCode;

	String message;

	String messageCode;

	@Override
	public void initialize(FieldValueExists fieldValue) {
		Class<? extends CrudRepository> clazz = fieldValue.repository();

		String respositoryQualifier = fieldValue.repositoryQualifier();

		this.required = fieldValue.required();
		this.requiredMsg = fieldValue.requiredMsg();
		this.message = fieldValue.message();
		this.requiredMsgCode = fieldValue.requiredMsgErorCode();
		this.messageCode = fieldValue.messageCode();

		if (!respositoryQualifier.equals("")) {
			this.repository = this.applicationContext.getBean(respositoryQualifier, clazz);
		} else {
			this.repository = this.applicationContext.getBean(clazz);
		}
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		var isEmpty = value == null;

		if (!isEmpty && value.getClass().getSimpleName().equals("String") && StringUtils.isBlank(value.toString()))
			isEmpty = true;

		if (isEmpty) {
			if (this.required)
				context.buildConstraintViolationWithTemplate(
						errorMessageUtils.getMessageWithCode(requiredMsg, new Object[] {}, requiredMsgCode))
						.addConstraintViolation();
			return !this.required;
		}

		Optional<Object> entity = this.repository.findById(value);

		if (entity.isEmpty()) {
			context.buildConstraintViolationWithTemplate(
					errorMessageUtils.getMessageWithCode(message, new Object[] { value }, messageCode))
					.addConstraintViolation();
			return false;
		}

		return true;
	}
}
