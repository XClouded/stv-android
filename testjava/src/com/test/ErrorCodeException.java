package com.test;
public class ErrorCodeException extends Exception {
	public ErrorCodeException(String errorInfo) {
		super(errorInfo);
	}
}