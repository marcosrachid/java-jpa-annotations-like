package com.ef.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ef.model.dao.exceptions.DriverNotFound;
import com.ef.util.DatabasePropertiesUtils;

/**
 * 
 * @author marcosrachid
 *
 */
public class DatabaseConnection {

	final static Logger LOG = Logger.getLogger(DatabaseConnection.class);

	private static Connection connection = null;

	/**
	 * Singleton internal instantiation
	 */
	private DatabaseConnection() {
	}

	/**
	 * Singleton connection instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (connection == null) {
			String driver = DatabasePropertiesUtils.getDriver();
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				throw new DriverNotFound(driver);
			}
			LOG.debug("Connected to [ " + DatabasePropertiesUtils.getURL() + " ] with username [ "
					+ DatabasePropertiesUtils.getUsername() + " ]");
			connection = DriverManager.getConnection(DatabasePropertiesUtils.getURL(),
					DatabasePropertiesUtils.getUsername(), DatabasePropertiesUtils.getPassword());
		}
		return connection;
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public static void close() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}
}
