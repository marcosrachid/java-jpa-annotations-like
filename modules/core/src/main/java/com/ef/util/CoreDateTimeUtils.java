package com.ef.util;

import java.time.format.DateTimeFormatter;

/**
 * Utils to handle application specific date type transformation and formatters
 * 
 * @author marcos
 *
 */
public class CoreDateTimeUtils extends DateTimeUtils {

	public static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	public static final DateTimeFormatter ARGUMENT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

}
