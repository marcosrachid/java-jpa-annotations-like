package com.ef.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ef.model.Access;
import com.ef.model.dao.crud.AbstractCrudDAO;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.util.DateTimeUtils;

/**
 * Access data access object
 * 
 * @author marcosrachid
 *
 */
public class AccessDAO extends AbstractCrudDAO<Access, Long> {

	final static Logger LOG = Logger.getLogger(AccessDAO.class);

	private static AccessDAO instance = null;

	/**
	 * Singleton internal instantiation
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private AccessDAO() throws SQLException {
		super(Access.class);
	}

	/**
	 * Singleton instantiation
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static AccessDAO getInstance() throws SQLException {
		if (instance == null) {
			instance = new AccessDAO();
		}
		return instance;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param threshold
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<BlockedIpDTO> findFilteredByArguments(LocalDateTime startDate, LocalDateTime endDate, Long threshold)
			throws SQLException {

		String sql = "SELECT PAR.IP_ID AS IP_ID, PI.IP AS IP, COUNT(PAR.IP_ID) AS COUNTER FROM PARSER_ACCESS_REGISTRY AS PAR "
				+ "INNER JOIN PARSER_IP AS PI ON (PAR.IP_ID = PI.ID) "
				+ "WHERE PAR.ACCESS_DATE BETWEEN ? AND ? GROUP BY PAR.IP_ID HAVING COUNT(PAR.IP_ID) >= ?";
		LOG.debug("findFilteredByArguments statement: " + sql);

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setTimestamp(1, DateTimeUtils.asTimestamp(startDate));
		preparedStatement.setTimestamp(2, DateTimeUtils.asTimestamp(endDate));
		preparedStatement.setLong(3, threshold);

		ResultSet result = preparedStatement.executeQuery();
		List<BlockedIpDTO> list = new ArrayList<>();
		// iterate each line from ResultSet
		while (result.next()) {
			Long ipId = result.getLong("IP_ID");
			String ip = result.getString("IP");
			Long counter = result.getLong("COUNTER");
			list.add(new BlockedIpDTO(ipId, ip, counter));
		}

		return list;
	}

}
