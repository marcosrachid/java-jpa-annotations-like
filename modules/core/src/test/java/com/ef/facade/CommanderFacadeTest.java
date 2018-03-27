package com.ef.facade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ef.config.DatabaseConnection;
import com.ef.services.exception.ServiceException;
import com.ef.util.enums.DurationEnum;

public class CommanderFacadeTest {

	private static final File ACCESS_LOG_EMPTY = new File(
			Thread.currentThread().getContextClassLoader().getResource("access_empty.log").getFile());

	private static final File ACCESS_2_LOG_EXAMPLE = new File(
			Thread.currentThread().getContextClassLoader().getResource("access2.log").getFile());

	private static final File ACCESS_HTTP_STATUS_NOT_INTEGER_LOG_EXAMPLE = new File(
			Thread.currentThread().getContextClassLoader().getResource("access_http_status_not_integer.log").getFile());

	private static final File ACCESS_WRONG_ACCESS_DATE_FORMAT_LOG_EXAMPLE = new File(Thread.currentThread()
			.getContextClassLoader().getResource("access_wrong_access_date_format.log").getFile());

	private static final LocalDateTime START_DATE = LocalDateTime.of(2007, Month.JANUARY, 1, 13, 0, 0, 0);

	private static final DurationEnum DURATION = DurationEnum.HOURLY;

	private static final Long THRESHOLD = 100L;

	private static final String DATABASE_SCRIPT_PATH = "sql/parser-1.0.0-ddl.sql";

	private CommanderFacade commanderFacade;

	private Server server;

	@Before
	public void setup() throws ClassNotFoundException, SQLException, FileNotFoundException {
		server = Server.createTcpServer("-tcpAllowOthers").start();
		RunScript.execute(DatabaseConnection.getConnection(), new FileReader(
				Thread.currentThread().getContextClassLoader().getResource(DATABASE_SCRIPT_PATH).getFile()));
	}

	@Test
	public void emptyLogTest() throws Exception {
		commanderFacade = new CommanderFacade();
		commanderFacade.accesslog = ACCESS_LOG_EMPTY;
		commanderFacade.startDate = START_DATE;
		commanderFacade.duration = DURATION;
		commanderFacade.threshold = THRESHOLD;
		commanderFacade.run();
	}

	@Test
	public void defaultLogTest() throws Exception {
		commanderFacade = new CommanderFacade();
		commanderFacade.startDate = START_DATE;
		commanderFacade.duration = DURATION;
		commanderFacade.threshold = THRESHOLD;
		commanderFacade.run();
	}

	@Test
	public void setArgsTest() throws Exception {
		commanderFacade = new CommanderFacade();
		commanderFacade.accesslog = ACCESS_2_LOG_EXAMPLE;
		commanderFacade.startDate = START_DATE;
		commanderFacade.duration = DURATION;
		commanderFacade.threshold = THRESHOLD;
		commanderFacade.run();
	}

	@Test(expected = ServiceException.class)
	public void wrongAccessDateLogTest() throws Exception {
		commanderFacade = new CommanderFacade();
		commanderFacade.accesslog = ACCESS_WRONG_ACCESS_DATE_FORMAT_LOG_EXAMPLE;
		commanderFacade.run();
	}

	@Test(expected = ServiceException.class)
	public void httpStatusNotIntegerLogTest() throws Exception {
		commanderFacade = new CommanderFacade();
		commanderFacade.accesslog = ACCESS_HTTP_STATUS_NOT_INTEGER_LOG_EXAMPLE;
		commanderFacade.run();
	}

	@After
	public void shutdown() {
		server.stop();
	}

}
