package com.zhenzhen.demo.springboot.common.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class BeanValidatorUtil {
	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	public static String check(Object object) throws RuntimeException {
		StringBuffer resutlStr = new StringBuffer();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Object>> validateResult = validator.validate(object,new Class[0]);
		if (!validateResult.isEmpty()) {
			Iterator<ConstraintViolation<Object>> iterator = validateResult.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<Object> violation = iterator.next();
				resutlStr.append(violation.getMessage()+",");
			}
		}
		if(resutlStr.length()>0) {
			resutlStr.deleteCharAt(resutlStr.length()-1);
			return resutlStr.toString();
		}
		return null;
	}
}