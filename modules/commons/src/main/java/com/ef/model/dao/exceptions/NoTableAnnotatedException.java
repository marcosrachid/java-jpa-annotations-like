package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class NoTableAnnotatedException extends SQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param clazz
	 */
	public NoTableAnnotatedException(Class<?> clazz) {
		super("No Table was annotated for [" + clazz.getName() + "]");
	}

}
