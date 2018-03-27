package com.ef.facade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.ef.model.dto.BlockedIpDTO;
import com.ef.services.AccessService;
import com.ef.services.IpService;
import com.ef.services.LogService;
import com.ef.services.exception.ServiceException;
import com.ef.util.CoreConstants;
import com.ef.util.converters.DateTimeConverter;
import com.ef.util.converters.DurationConverter;
import com.ef.util.enums.DurationEnum;

/**
 * Execute parser based on arguments
 * 
 * @author marcosrachid
 *
 */
@Parameters(separators = "=")
public class CommanderFacade {

	@Parameter(names = { "--accesslog" }, converter = FileConverter.class)
	File accesslog = null;

	@Parameter(names = { "--startDate" }, converter = DateTimeConverter.class, required = true)
	LocalDateTime startDate;

	@Parameter(names = { "--duration" }, converter = DurationConverter.class, required = true)
	DurationEnum duration;

	@Parameter(names = { "--threshold" }, required = true)
	Long threshold;
	
	@Parameter(names = { "--log" })
	boolean log = false;

	private AccessService accessService;

	private IpService ipService;

	private LogService logService;

	/**
	 * Constructor
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public CommanderFacade() throws SQLException {
		accessService = AccessService.getInstance();
		ipService = IpService.getInstance();
		logService = LogService.getInstance();
	}

	/**
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws InstantiationException
	 * @throws ServiceException
	 */
	public void run() throws ServiceException, SQLException {
		// show debug log
		if (log) {
			LogManager.getRootLogger().setLevel(Level.DEBUG);
		}
		// necessary to read default log from jars when accesslog argument not set
		readFile();
		// save to database new Ips and return a map key: ip, value: ipId from all
		// registered ips
		Map<String, Long> ipMap = ipService.loadIps(accesslog);
		// save new accesses from access log to database
		accessService.loadAccessLog(accesslog, ipMap);
		// find on database the ips that must be blocked based on application arguments
		List<BlockedIpDTO> blockedIps = accessService.findFilteredByArguments(startDate, duration, threshold);
		// change status of the found blocked ips
		ipService.blockIps(blockedIps, threshold);
		// log to server with the reason why the ip was blocked
		logService.logBlockedIps(blockedIps, threshold);
	}

	/**
	 * 
	 * @throws ServiceException
	 */
	private void readFile() throws ServiceException {
		if (accesslog == null) {
			InputStream inputStream = null;
			OutputStream out = null;
			try {
				accesslog = File.createTempFile(CoreConstants.ACCESS_FILE_PREFIX, CoreConstants.ACCESS_FILE_SUFFIX);
				accesslog.deleteOnExit();
				inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(CoreConstants.ACCESS_FILE);
				out = new FileOutputStream(accesslog);
				byte buf[] = new byte[1024];
				int len;
				while ((len = inputStream.read(buf)) > 0)
					out.write(buf, 0, len);
			} catch (IOException e) {
				throw new ServiceException("Could not read file default file access.log");
			} finally {
				try {
					if (out != null)
						out.close();
					if (inputStream != null)
						inputStream.close();
				} catch (IOException e) {
					throw new ServiceException("Could not close streams");
				}
			}
		}
	}
}
