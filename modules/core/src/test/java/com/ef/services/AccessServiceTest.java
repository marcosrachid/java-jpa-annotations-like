package com.ef.services;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.ef.model.Access;
import com.ef.model.dao.AccessDAO;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.services.exception.ServiceException;
import com.ef.util.enums.DurationEnum;

public class AccessServiceTest {
	
	private static final File ACCESS_LOG_EXAMPLE = new File(
			Thread.currentThread().getContextClassLoader().getResource("access_minimum.log").getFile());

	@Spy
	private AccessDAO dao;

	@InjectMocks
	private AccessService accessService;

	@Before
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(dao).insert(Matchers.anyListOf(Access.class));
		Mockito.doReturn(getBlockedIps()).when(dao).findFilteredByArguments(Matchers.any(LocalDateTime.class),
				Matchers.any(LocalDateTime.class), Matchers.anyLong());
	}

	@Test
	public void loadAccessLogTest() throws ServiceException, SQLException {
		accessService.loadAccessLog(ACCESS_LOG_EXAMPLE, getIpMap());
	}

	@Test
	public void findFilteredByArguments() throws SQLException {
		List<BlockedIpDTO> blockedIps = accessService.findFilteredByArguments(LocalDateTime.of(2007, Month.JANUARY, 1, 13, 0, 0, 0), DurationEnum.HOURLY, 100L);
		assertThat(blockedIps.size(), equalTo(2));
	}
	
	private Map<String, Long> getIpMap() {
		Map<String, Long> ipMap = new HashMap<>();
		ipMap.put("192.168.234.82", 1L);
		ipMap.put("192.168.169.194", 2L);
		return ipMap;
	}

	private List<BlockedIpDTO> getBlockedIps() {
		List<BlockedIpDTO> blockedIps = new ArrayList<>();
		blockedIps.add(new BlockedIpDTO(1L, "192.168.77.101", 150L));
		blockedIps.add(new BlockedIpDTO(1L, "192.168.228.188", 200L));
		return blockedIps;
	}

}
