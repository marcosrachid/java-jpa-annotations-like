package com.ef;

import java.sql.SQLException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.ef.config.DatabaseConnection;
import com.ef.facade.CommanderFacade;
import com.ef.services.exception.ServiceException;

/**
 * Executable main class
 * 
 * @author marcosrachid
 *
 */
public class Parser {

	/**
	 * Executable method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CommanderFacade commander = new CommanderFacade();
			JCommander.newBuilder().addObject(commander).build().parse(args);
			commander.run();
		} catch (ParameterException e) {
			System.out.println("Required parameters: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
		} catch (ServiceException e) {
			System.out.println("Service error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unamapped error: " + e.getMessage());
		} finally {
			try {
				DatabaseConnection.close();
			} catch (SQLException e) {
				System.out.println("Error ocurred when trying to close connection.");
			}
		}
	}

}
