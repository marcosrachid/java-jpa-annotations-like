package com.ef.model.dao.prepare;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ef.model.annotations.Column;
import com.ef.model.annotations.CreateDate;
import com.ef.model.annotations.Id;
import com.ef.model.annotations.Table;
import com.ef.model.dao.exceptions.InaccessibleValueException;
import com.ef.model.dao.exceptions.InappropriateValueException;
import com.ef.util.DatabaseDataTypeMappingUtils;

/**
 * Helper Abstract class to prepare crud statements
 * 
 * @author marcosrachid
 *
 * @param <T>
 */
public abstract class AbstractPrepareDAO<T extends Serializable, PK> {

	/**
	 * 
	 * @param result
	 * @param clazz
	 * @param fields
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws InstantiationException
	 */
	protected T generateInstance(ResultSet result, Class<T> clazz) throws SQLException {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column == null)
				continue;
			field.setAccessible(true);
			set(field, instance, DatabaseDataTypeMappingUtils.getStatementParameter(field, result, column));
		}
		return instance;
	}

	/**
	 * 
	 * @param table
	 * @param fields
	 * @return
	 */
	protected String generateInsertQuery(Table table, Field[] fields) {
		StringBuilder sql = new StringBuilder();
		StringBuilder references = new StringBuilder();

		sql.append("INSERT INTO " + table.name() + "(");
		// loop to get each column name
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Column column = field.getAnnotation(Column.class);
			// Non-columns, Id and CreateDate does not need to be listed on query
			if (column == null || field.isAnnotationPresent(Id.class) || (field.isAnnotationPresent(CreateDate.class)))
				continue;
			sql.append(column.name() + ", ");
			references.append("?, ");
		}
		// remove comma
		sql.delete(sql.length() - 2, sql.length());
		references.delete(references.length() - 2, references.length());
		sql.append(") VALUES (" + references.toString() + ")");

		return sql.toString();
	}

	/**
	 * 
	 * @param table
	 * @param columnId
	 * @param fields
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected String generateUpdateQuery(Table table, Column columnId, Field[] fields, T entity) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("UPDATE " + table.name() + " SET ");
		PK id = null;
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			field.setAccessible(true);
			Object object = get(field, entity);
			if (object == null || column == null || (field.isAnnotationPresent(CreateDate.class))) {
				continue;
			}
			if (field.isAnnotationPresent(Id.class)) {
				id = (PK) object;
				continue;
			}
			sql.append(column.name() + "= ?, ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(" WHERE " + columnId.name() + " = " + id);

		return sql.toString();
	}

	/**
	 * 
	 * @param preparedStatement
	 * @param fields
	 * @param entity
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws InstantiationException
	 */
	protected void prepare(final PreparedStatement preparedStatement, Field[] fields, T entity) throws SQLException {
		for (int index = 0, decrement = 0; index < fields.length; index++) {
			Field field = fields[index];
			field.setAccessible(true);
			Object object = get(field, entity);
			Column column = field.getAnnotation(Column.class);
			if (column == null || field.isAnnotationPresent(Id.class)
					|| (field.isAnnotationPresent(CreateDate.class))) {
				decrement++;
				continue;
			}
			DatabaseDataTypeMappingUtils.setStatementParameter(preparedStatement, (index - decrement + 1), field,
					object);
		}

	}

	/**
	 * 
	 * @param preparedStatement
	 * @param fields
	 * @param entity
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws InstantiationException
	 */
	protected void prepareUpdate(final PreparedStatement preparedStatement, Field[] fields, T entity)
			throws SQLException {
		for (int index = 0, decrement = 0; index < fields.length; index++) {
			Field field = fields[index];
			field.setAccessible(true);
			Object object = get(field, entity);
			Column column = field.getAnnotation(Column.class);
			if (object == null || column == null || field.isAnnotationPresent(Id.class)
					|| (field.isAnnotationPresent(CreateDate.class))) {
				decrement++;
				continue;
			}
			DatabaseDataTypeMappingUtils.setStatementParameter(preparedStatement, (index - decrement + 1), field,
					object);
		}

	}

	/**
	 * 
	 * @param field
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	protected Object get(Field field, Object entity) throws SQLException {
		try {
			return field.get(entity);
		} catch (IllegalArgumentException e) {
			throw new InappropriateValueException();
		} catch (IllegalAccessException e) {
			throw new InaccessibleValueException();
		}
	}

	/**
	 * 
	 * @param field
	 * @param entity
	 * @param value
	 * @throws SQLException
	 */
	protected void set(final Field field, Object entity, Object value) throws SQLException {
		try {
			field.set(entity, value);
		} catch (IllegalArgumentException e) {
			throw new InappropriateValueException();
		} catch (IllegalAccessException e) {
			throw new InaccessibleValueException();
		}
	}

}
