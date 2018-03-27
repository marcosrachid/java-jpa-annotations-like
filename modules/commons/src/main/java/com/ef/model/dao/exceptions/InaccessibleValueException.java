package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class InaccessibleValueException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	public InaccessibleValueException() {
		super("Value is inaccessible to the correspondent field from class");
	}

}
