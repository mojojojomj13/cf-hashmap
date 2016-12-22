package com.techno.broker.exception;

import org.springframework.http.HttpStatus;

/**
 * A Custom Exception that represents a Service Error Condition
 * 
 * @author Prithvish Mukherjee
 *
 */
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5708696979892146999L;
	private String message;
	private Throwable throwable;
	private HttpStatus status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public ServiceException() {
	}

	public ServiceException(String message, Throwable throwable, HttpStatus status) {
		this.message = message;
		this.throwable = throwable;
		this.status = status;
	}

	public ServiceException(String message, Throwable throwable) {
		this.message = message;
		this.throwable = throwable;
	}

}
