package com.ef.model.dao;

import java.sql.SQLException;

import com.ef.model.Ip;
import com.ef.model.dao.crud.AbstractCrudDAO;

/**
 * Ip data access object
 * 
 * @author marcosrachid
 *
 */
public class IpDAO extends AbstractCrudDAO<Ip, Long> {

	private static IpDAO instance = null;

	/**
	 * Singleton internal instantiation
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private IpDAO() throws SQLException {
		super(Ip.class);
	}

	/**
	 * Singleton instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static IpDAO getInstance() throws SQLException {
		if (instance == null) {
			instance = new IpDAO();
		}
		return instance;
	}

}
