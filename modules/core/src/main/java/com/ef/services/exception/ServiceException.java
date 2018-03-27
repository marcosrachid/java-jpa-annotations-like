package com.ef.services.exception;

/**
 * Runtime services related exception
 * 
 * @author marcosrachid
 *
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ServiceException(String message) {
		super(message);
	}

}
