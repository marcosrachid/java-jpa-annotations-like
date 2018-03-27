package com.ef.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.ef.model.Access;
import com.ef.model.dao.AccessDAO;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.services.exception.ServiceException;
import com.ef.util.CoreDateTimeUtils;
import com.ef.util.StringUtils;
import com.ef.util.enums.DurationEnum;

/**
 * Access rule handler
 * 
 * @author marcosrachid
 *
 */
public class AccessService {

	private static AccessService instance = null;

	private AccessDAO dao;

	/**
	 * Singleton internal instantiation
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private AccessService() throws SQLException {
		dao = AccessDAO.getInstance();
	}

	/**
	 * Singleton instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static AccessService getInstance() throws SQLException {
		if (instance == null) {
			instance = new AccessService();
		}
		return instance;
	}

	/**
	 * 
	 * @param accesslog
	 * @param ipMap
	 * @throws ServiceException
	 * @throws SQLException
	 */
	public void loadAccessLog(File accesslog, Map<String, Long> ipMap) throws ServiceException, SQLException {
		List<Access> accesses = new ArrayList<>();
		Scanner scanner;
		try {
			scanner = new Scanner(accesslog);
		} catch (FileNotFoundException e) {
			throw new ServiceException("Log file not found");
		}
		while (scanner.hasNextLine()) {
			Access access = stringToEntity(scanner.nextLine(), ipMap);
			accesses.add(access);
		}
		scanner.close();
		dao.insert(accesses);
	}

	/**
	 * 
	 * @param startDate
	 * @param duration
	 * @param threshold
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public List<BlockedIpDTO> findFilteredByArguments(LocalDateTime startDate, DurationEnum duration, Long threshold)
			throws SQLException {
		LocalDateTime endDate = null;
		switch (duration) {
		case DAILY:
			endDate = startDate.plusDays(1L).minusSeconds(1L);
			break;
		case HOURLY:
			endDate = startDate.plusHours(1L).minusSeconds(1L);
			break;
		}
		return dao.findFilteredByArguments(startDate, endDate, threshold);
	}

	/**
	 * 
	 * @param line
	 * @param ipMap
	 * @return
	 * @throws ServiceException
	 */
	private Access stringToEntity(String line, Map<String, Long> ipMap) throws ServiceException {
		String[] splitted = line.split("\\|");
		LocalDateTime accessDate = null;
		try {
			accessDate = LocalDateTime.parse(splitted[0], CoreDateTimeUtils.LOG_DATE_FORMAT);
		} catch (DateTimeParseException e) {
			throw new ServiceException("Access date from log is with a wrong format");
		}
		Long ipId = ipMap.get(splitted[1]);
		String httpMethod = StringUtils.removeQuotes(splitted[2]);
		Integer httpStatus = null;
		try {
			httpStatus = Integer.valueOf(splitted[3]);
		} catch (NumberFormatException e) {
			throw new ServiceException("HTTP status from log is not integer");
		}
		String accessMeans = StringUtils.removeQuotes(splitted[4]);

		return new Access(accessDate, ipId, httpMethod, httpStatus, accessMeans);
	}

}
