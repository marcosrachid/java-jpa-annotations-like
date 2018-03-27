package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class DuplicateColumnNameException extends SQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param clazz
	 * @param name
	 */
	public DuplicateColumnNameException(Class<?> clazz, String name) {
		super("[" + clazz.getName() + "] has duplicated column names with name [" + name + "]");
	}

}
