package com.ef.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.ef.model.Ip;
import com.ef.model.dao.IpDAO;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.services.exception.ServiceException;

/**
 * Ip rule handler
 * 
 * @author marcosrachid
 *
 */
public class IpService {

	private static IpService instance = null;

	private IpDAO dao;

	/**
	 * Singleton internal instantiation
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private IpService() throws SQLException {
		dao = IpDAO.getInstance();
	}

	/**
	 * Singleton instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static IpService getInstance() throws SQLException {
		if (instance == null) {
			instance = new IpService();
		}
		return instance;
	}

	public Map<String, Long> loadIps(File accesslog) throws ServiceException, SQLException {
		Set<Ip> ips = new HashSet<>();

		Scanner scanner;
		try {
			scanner = new Scanner(accesslog);
		} catch (FileNotFoundException e) {
			throw new ServiceException("Log file not found");
		}
		while (scanner.hasNextLine()) {
			ips.add(stringToEntity(scanner.nextLine()));
		}
		scanner.close();

		// get existing Ips from database
		List<Ip> dataIps = dao.find();

		// convert set to List
		List<Ip> ipList = new ArrayList<Ip>(ips);

		// get list of new ips from accesslog
		ipList.removeAll(dataIps);

		// insert new ips on database
		dao.insert(ipList);

		// add database ips to the new added ips from accesslog
		ipList.addAll(dataIps);

		return ipList.stream().collect(Collectors.toMap(i -> i.getIp(), i -> i.getId()));

	}

	public void blockIps(List<BlockedIpDTO> blockedIps, Long threshold) throws SQLException {
		for (BlockedIpDTO blockedIp : blockedIps) {
			dao.update(new Ip(blockedIp.getIpId(), false, LocalDateTime.now()));
			System.out.println("Blocked IP: " + blockedIp.getIp());
		}
	}

	private Ip stringToEntity(String line) {
		String[] splitted = line.split("\\|");
		String ip = splitted[1];
		return new Ip(ip, true);
	}

}
