package com.ef.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ef.model.Log;
import com.ef.model.dao.LogDAO;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.util.CoreConstants;

/**
 * Log rule handler
 * 
 * @author marcosrachid
 *
 */
public class LogService {

	private static LogService instance = null;

	private LogDAO dao;

	/**
	 * Singleton internal instantiation
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private LogService() throws SQLException {
		dao = LogDAO.getInstance();
	}

	/**
	 * Singleton instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static LogService getInstance() throws SQLException {
		if (instance == null) {
			instance = new LogService();
		}
		return instance;
	}

	/**
	 * 
	 * @param accesses
	 * @param ipMap
	 * @param threshold
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void logBlockedIps(List<BlockedIpDTO> blockedIps, Long threshold) throws SQLException {
		List<Log> logs = new ArrayList<>();
		for (BlockedIpDTO blockedIp : blockedIps) {
			String comment = String.format(CoreConstants.BLOCK_MESSAGE, blockedIp.getIp(), threshold);
			logs.add(new Log(blockedIp.getIpId(), comment));
		}
		dao.insert(logs);
	}

}
