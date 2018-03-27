package com.ef.model.dao.exceptions;

import java.sql.SQLException;

public class MoreThenOneItemException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	public MoreThenOneItemException() {
		super("More then one item was found from result");
	}
	
}
