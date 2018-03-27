package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class NoIdAnnotatedException extends SQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param clazz
	 */
	public NoIdAnnotatedException(Class<?> clazz) {
		super("No Id was annotated for [" + clazz.getName() + "]");
	}

}
