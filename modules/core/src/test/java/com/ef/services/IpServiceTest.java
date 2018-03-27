package com.ef.services;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ef.model.Ip;
import com.ef.model.dao.IpDAO;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.services.exception.ServiceException;

public class IpServiceTest {

	private static final File ACCESS_LOG_EXAMPLE = new File(
			Thread.currentThread().getContextClassLoader().getResource("access.log").getFile());

	@Spy
	private IpDAO dao;

	@InjectMocks
	private IpService ipService;

	@Before
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		Mockito.doReturn(new ArrayList<Ip>()).when(dao).find();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				@SuppressWarnings("unchecked")
				List<Ip> ips = (List<Ip>) invocation.getArguments()[0];
				Long[] id = { 1L };
				ips.forEach(i -> {
					i.setId(id[0]);
					id[0]++;
				});
				return null;
			}
		}).when(dao).insert(Matchers.anyListOf(Ip.class));
		Mockito.doNothing().when(dao).update(Matchers.any(Ip.class));
	}

	@Test
	public void loadIpsTest() throws ServiceException, SQLException {
		Map<String, Long> ipMap = ipService.loadIps(ACCESS_LOG_EXAMPLE);
		assertThat(ipMap.entrySet().size(), equalTo(929));
	}

	@Test
	public void blockIpsTest() throws SQLException {
		ipService.blockIps(getBlockedIps(), 100L);
	}

	private List<BlockedIpDTO> getBlockedIps() {
		List<BlockedIpDTO> blockedIps = new ArrayList<>();
		blockedIps.add(new BlockedIpDTO(1L, "192.168.77.101", 150L));
		blockedIps.add(new BlockedIpDTO(1L, "192.168.228.188", 200L));
		return blockedIps;
	}

}
