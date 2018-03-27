package com.ef.util.converters;

import java.time.LocalDateTime;

import com.beust.jcommander.IStringConverter;
import com.ef.util.CoreDateTimeUtils;

/**
 * Argument converter from string to LocalDateTime
 * 
 * @author marcosrachid
 *
 */
public class DateTimeConverter implements IStringConverter<LocalDateTime> {

	@Override
	public LocalDateTime convert(String datetime) {
		return LocalDateTime.parse(datetime, CoreDateTimeUtils.ARGUMENT_DATE_FORMAT);
	}

}
