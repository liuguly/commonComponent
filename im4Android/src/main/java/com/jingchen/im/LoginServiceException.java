/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.jingchen.im;

/**
 * Service层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * 
 * @author calvin
 */
public class LoginServiceException extends RuntimeException {

	private static final long serialVersionUID = 3583566093089790852L;

	public LoginServiceException() {
		super();
	}

	public LoginServiceException(String message) {
		super(message);
	}

	public LoginServiceException(Throwable cause) {
		super(cause);
	}

	public LoginServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
