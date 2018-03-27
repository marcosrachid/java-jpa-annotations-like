package com.ef.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.BooleanUtils;

import com.ef.model.annotations.Column;
import com.ef.model.dao.exceptions.UnmappedClassTypeException;

/**
 * Utils to map datatypes between Java and SQL
 * 
 * @author marcosrachid
 *
 */
public class DatabaseDataTypeMappingUtils {

	public static Object getStatementParameter(Field field, ResultSet result, Column column) throws SQLException {
		Class<?> type = field.getType();
		switch (MappedClassesEnum.valueOf(type.getSimpleName().toUpperCase())) {
		case INTEGER:
			return result.getInt(column.name());
		case LONG:
			return result.getLong(column.name());
		case BIGDECIMAL:
			return result.getBigDecimal(column.name());
		case LOCALDATE:
			Date date = result.getDate(column.name());
			if (date != null)
				return result.getDate(column.name()).toLocalDate();
			return null;
		case LOCALDATETIME:
			Timestamp timestamp = result.getTimestamp(column.name());
			if (timestamp != null)
				return result.getTimestamp(column.name()).toLocalDateTime();
			return null;
		case STRING:
			return result.getString(column.name());
		case BOOLEAN:
			return BooleanUtils.toBoolean(result.getInt(column.name()));
		default:
			throw new UnmappedClassTypeException(type);
		}
	}

	public static void setStatementParameter(PreparedStatement preparedStatement, int index, Field field, Object object)
			throws UnmappedClassTypeException, SQLException {
		if (object == null) {
			preparedStatement.setNull(index, mapTypes(field.getType(), object));
			return;
		}
		Class<?> type = field.getType();
		switch (MappedClassesEnum.valueOf(field.getType().getSimpleName().toUpperCase())) {
		case INTEGER:
			preparedStatement.setInt(index, (Integer) object);
			break;
		case LONG:
			preparedStatement.setLong(index, (Long) object);
			break;
		case BIGDECIMAL:
			preparedStatement.setBigDecimal(index, (BigDecimal) object);
			break;
		case LOCALDATE:
			preparedStatement.setDate(index, DateTimeUtils.asSQLDate((LocalDate) object));
			break;
		case LOCALDATETIME:
			preparedStatement.setTimestamp(index, DateTimeUtils.asTimestamp((LocalDateTime) object));
			break;
		case STRING:
			preparedStatement.setString(index, (String) object);
			break;
		case BOOLEAN:
			preparedStatement.setInt(index, (Boolean) object ? 1 : 0);
			break;
		default:
			throw new UnmappedClassTypeException(type);
		}
		return;
	}

	public static int mapTypes(Class<?> type, Object object) throws UnmappedClassTypeException {
		switch (MappedClassesEnum.valueOf(type.getSimpleName().toUpperCase())) {
		case INTEGER:
			return Types.INTEGER;
		case LONG:
			return Types.BIGINT;
		case BIGDECIMAL:
			return Types.DECIMAL;
		case LOCALDATE:
			return Types.DATE;
		case LOCALDATETIME:
			return Types.TIMESTAMP;
		case STRING:
			return Types.VARCHAR;
		case BOOLEAN:
			return Types.TINYINT;
		default:
			throw new UnmappedClassTypeException(type);
		}
	}

	private enum MappedClassesEnum {
		INTEGER, LONG, BIGDECIMAL, LOCALDATE, LOCALDATETIME, STRING, BOOLEAN;
	}

}
