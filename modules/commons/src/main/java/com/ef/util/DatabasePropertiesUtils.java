package com.ef.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read db.properties to set connection parameters
 * 
 * @author marcosrachid
 *
 */
public class DatabasePropertiesUtils {
	
	private static Properties prop = new Properties();
	
	static {
		InputStream resourceStream = null;
		try {
			resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CommonsConstants.DB_PROPERTIES_RESOURCE);
			prop.load(resourceStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (resourceStream != null) {
				try {
					resourceStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getDriver() {
		return prop.getProperty("connection.driver");
	}
	
	public static String getURL() {
		return prop.getProperty("connection.url");
	}
	
	public static String getUsername() {
		return prop.getProperty("connection.username"); 
	}
	
	public static String getPassword() {
		return prop.getProperty("connection.password"); 
	}

}
