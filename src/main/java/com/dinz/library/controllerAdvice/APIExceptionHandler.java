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
import com.dinz.library.exception.DeathUserException;
import com.dinz.library.exception.UploadImageException;

@RestControllerAdvice
public class APIExceptionHandler {

	@ExceptionHandler(value = RuntimeException.class)
	public APIResult handleRuntimeException(Exception ex) {
		ex.printStackTrace();
		return new APIResult(APIResultMessage.FAILED, ex.getMessage());
	}

	@ExceptionHandler(value = UploadImageException.class)
	public APIResult handleUploadImageException(Exception ex) {
		return new APIResult(APIResultMessage.ofFailed(ex.getMessage()));
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

	@ExceptionHandler(javax.persistence.NoResultException.class)
	public APIResult handleNoResultException(Exception ex) {
		return new APIResult(APIResultMessage.NOT_FOUND_ITEM, "");
	}

	@ExceptionHandler(DeathUserException.class)
	public Object handleDeathUser(DeathUserException ex) {
		throw ex;
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public APIResult handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
		return new APIResult(new APIResultMessage(-2, "Bạn không có quyền với tài khoản này"), "");
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public APIResult handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return new APIResult(APIResultMessage.FAILED, APIUtils.getErrorDataIntegrityViolation(ex));
	}
}
