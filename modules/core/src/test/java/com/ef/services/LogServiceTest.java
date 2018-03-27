package com.ef.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.ef.model.Log;
import com.ef.model.dao.LogDAO;
import com.ef.model.dto.BlockedIpDTO;

public class LogServiceTest {

	@Spy
	private LogDAO dao;

	@InjectMocks
	private LogService logService;

	@Before
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(dao).insert(Matchers.anyListOf(Log.class));
	}

	@Test
	public void test() throws SQLException {
		logService.logBlockedIps(getBlockedIps(), 100L);
	}
	
	private List<BlockedIpDTO> getBlockedIps() {
		List<BlockedIpDTO> blockedIps = new ArrayList<>();
		blockedIps.add(new BlockedIpDTO(1L, "192.168.135.224", 50L));
		blockedIps.add(new BlockedIpDTO(1L, "192.168.246.194", 750L));
		blockedIps.add(new BlockedIpDTO(1L, "192.168.187.167", 100L));
		blockedIps.add(new BlockedIpDTO(1L, "192.168.251.152", 150L));
		blockedIps.add(new BlockedIpDTO(1L, "192.168.136.242", 200L));
		return blockedIps;
	}

}
