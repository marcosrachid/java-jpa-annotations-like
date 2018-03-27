package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class CreateDateClassTypeException extends SQLException {

	private static final long serialVersionUID = 1L;

	public CreateDateClassTypeException() {
		super("DateCreate annotation musta have a LocalDateTime class type");
	}
	
	

}
