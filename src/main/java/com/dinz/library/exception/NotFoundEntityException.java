package com.dinz.library.exception;

public class NotFoundEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundEntityException() {
		super("Không tìm thấy đối tượng");
	}
}
