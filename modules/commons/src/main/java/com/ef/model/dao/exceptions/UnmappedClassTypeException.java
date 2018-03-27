package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class UnmappedClassTypeException extends SQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param clazz
	 */
	public UnmappedClassTypeException(Class<?> clazz) {
		super("Class type [" + clazz.getName() + "] could not be converted to SQL type");
	}

}
