package com.dinz.library.exception;

public class UploadImageException extends RuntimeException {

	private static final long serialVersionUID = -3786465380108838142L;

	public UploadImageException() {
		super("Hình ảnh không hợp lệ");
	}
}
