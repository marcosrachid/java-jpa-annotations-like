package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class NoColumnAnnotatedException extends SQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param clazz
	 */
	public NoColumnAnnotatedException(Class<?> clazz) {
		super("No Column was annotated for [" + clazz.getName() + "]");
	}

}
