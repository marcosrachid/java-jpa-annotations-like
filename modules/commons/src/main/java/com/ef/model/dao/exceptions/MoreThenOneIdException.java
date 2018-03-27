package com.ef.model.dao.exceptions;

import java.sql.SQLException;

/**
 * 
 * @author marcosrachid
 *
 */
public class MoreThenOneIdException extends SQLException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param clazz
	 */
	public MoreThenOneIdException(Class<?> clazz) {
		super("More then one Id was annotated for [" + clazz.getName() + "]");
	}

}
