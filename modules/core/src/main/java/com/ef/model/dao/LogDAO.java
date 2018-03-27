package com.ef.model.dao;

import java.sql.SQLException;

import com.ef.model.Log;
import com.ef.model.dao.crud.AbstractCrudDAO;

/**
 * Log data access object
 * 
 * @author marcosrachid
 *
 */
public class LogDAO extends AbstractCrudDAO<Log, Long> {

	private static LogDAO instance = null;

	/**
	 * Singleton internal instantiation
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private LogDAO() throws SQLException {
		super(Log.class);
	}

	/**
	 * Singleton instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static LogDAO getInstance() throws SQLException {
		if (instance == null) {
			instance = new LogDAO();
		}
		return instance;
	}
}
