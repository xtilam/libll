package com.dinz.library.controllerAdvice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.dinz.library.api.admin.APIUtils;
import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;

@RestControllerAdvice
public class APIExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public APIResult handleRuntimeException(Exception ex, WebRequest request) {
		return new APIResult(APIResultMessage.FAILED, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public APIResult handleAllException(Exception ex, WebRequest request) {
		return new APIResult(APIResultMessage.FAILED, ex.getMessage());
	}

	@ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
	public APIResult handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException ex) {
		return new APIResult(APIResultMessage.FAILED, APIUtils.getErrorDataIntegrityViolation(ex));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public APIResult handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return new APIResult(APIResultMessage.FAILED, APIUtils.getErrorDataIntegrityViolation(ex));
	}
}
