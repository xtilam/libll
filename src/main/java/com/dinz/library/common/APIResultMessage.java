package com.dinz.library.common;

import java.util.HashMap;

import org.springframework.data.util.Pair;

public class APIResultMessage implements IAPIResultMessage {
	private int code;
	private String content;

	private HashMap<String, Object> data = new HashMap<>();

	public APIResultMessage() {
	}

	public APIResultMessage(int code, String content) {
		this.code = code;
		this.content = content;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public APIResultMessage appendData(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	public static APIResultMessage of(Pair<Integer, String> data) {
		try {
			return new APIResultMessage(data.getFirst(), data.getSecond());
		} catch (Exception e) {
			return null;
		}
	}

	public static APIResultMessage ofSuccess(String message) {
		return new APIResultMessage(0, message);
	}

	public static APIResultMessage ofFailed(String message) {
		return new APIResultMessage(1, message);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}