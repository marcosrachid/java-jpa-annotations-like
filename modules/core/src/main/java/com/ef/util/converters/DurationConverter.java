package com.ef.util.converters;

import com.beust.jcommander.IStringConverter;
import com.ef.util.enums.DurationEnum;

/**
 * Argument converter from string to DurationEnum
 * 
 * @author marcosrachid
 *
 */
public class DurationConverter implements IStringConverter<DurationEnum>{

	@Override
	public DurationEnum convert(String duration) {
		return DurationEnum.valueOf(duration.toUpperCase());
	}

}
