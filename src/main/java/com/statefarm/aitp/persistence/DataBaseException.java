package com.statefarm.aitp.persistence;

public class DataBaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataBaseException(String message, Throwable e) {
		super(message, e);
	}

	public DataBaseException(String message) {
		super(message);
	}
}
