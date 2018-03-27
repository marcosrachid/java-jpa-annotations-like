package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class DriverNotFound extends SQLException {

	private static final long serialVersionUID = 1L;
	
	public DriverNotFound(String driver) {
		super("Driver [" + driver + "] not found");
	}

}
