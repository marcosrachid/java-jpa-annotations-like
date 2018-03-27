package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class InappropriateValueException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	public InappropriateValueException() {
		super("Value is inappropriate to the correspondent entity class");
	}

}
