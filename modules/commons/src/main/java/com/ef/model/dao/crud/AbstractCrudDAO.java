package com.ef.model.dao.crud;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ef.config.DatabaseConnection;
import com.ef.model.annotations.Column;
import com.ef.model.annotations.CreateDate;
import com.ef.model.annotations.Id;
import com.ef.model.annotations.Table;
import com.ef.model.dao.exceptions.CreateDateClassTypeException;
import com.ef.model.dao.exceptions.DuplicateColumnNameException;
import com.ef.model.dao.exceptions.MoreThenOneIdException;
import com.ef.model.dao.exceptions.MoreThenOneItemException;
import com.ef.model.dao.exceptions.NoColumnAnnotatedException;
import com.ef.model.dao.exceptions.NoIdAnnotatedException;
import com.ef.model.dao.exceptions.NoTableAnnotatedException;
import com.ef.model.dao.prepare.AbstractPrepareDAO;

/**
 * Abstract class with crud methods
 * 
 * @author marcosrachid
 *
 * @param <T>
 *            Table object representation
 * @param <PK>
 *            Object referred to primary key
 */
public abstract class AbstractCrudDAO<T extends Serializable, PK> extends AbstractPrepareDAO<T, PK> {

	final static Logger LOG = Logger.getLogger(AbstractCrudDAO.class);

	protected final Class<T> clazz;
	protected Table table;
	protected Column columnId;
	protected Field fieldId;

	protected final Connection connection;

	/**
	 * 
	 * @param clazz
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected AbstractCrudDAO(Class<T> clazz) throws SQLException {
		connection = DatabaseConnection.getConnection();
		this.clazz = clazz;
		this.table = clazz.getAnnotation(Table.class);
		Map<String, Column> columnMap = new HashMap<>();

		// Table must be annotated on class
		if (this.table == null) {
			throw new NoTableAnnotatedException(clazz);
		}

		// class column properties validation
		for (Field field : clazz.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			// if is a column then validate
			if (column != null) {
				if (field.isAnnotationPresent(Id.class)) {
					// Table must not have more then one Id
					if (this.columnId != null)
						throw new MoreThenOneIdException(clazz);
					this.columnId = column;
					this.fieldId = field;
					this.fieldId.setAccessible(true);
				}
				// createdate property must be a LocalDateTime
				if (field.isAnnotationPresent(CreateDate.class) && field.getType() != LocalDateTime.class) {
					throw new CreateDateClassTypeException();
				}
				// Table must not have Columns with the same name
				if (columnMap.containsKey(column.name())) {
					throw new DuplicateColumnNameException(clazz, column.name());
				}
				columnMap.put(column.name(), column);
			}
		}

		// Table must have an Id
		if (this.columnId == null) {
			throw new NoIdAnnotatedException(clazz);
		}

		// Table must have at least one column
		if (columnMap.isEmpty()) {
			throw new NoColumnAnnotatedException(clazz);
		}
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public List<T> find() throws SQLException {
		Statement statement = connection.createStatement();
		String sql = "SELECT * FROM " + table.name();
		LOG.debug("find statement: " + sql);
		ResultSet result = statement.executeQuery(sql);
		List<T> list = new ArrayList<>();
		// iterate each line from ResultSet
		while (result.next()) {
			// get instance type from ResultSet
			list.add(generateInstance(result, clazz));
		}
		return list;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public T findOne(PK id) throws SQLException {
		Statement statement = connection.createStatement();
		String sql = "SELECT * FROM " + table.name() + " WHERE " + columnId.name() + " = " + id;
		LOG.debug("findOne statement: " + sql);
		ResultSet result = statement.executeQuery(sql);
		T instance = null;
		// iterate each line from ResultSet
		while (result.next()) {
			// instance already set means more then one line was returned
			if (instance != null) {
				throw new MoreThenOneItemException();
			}
			// get instance type from ResultSet
			instance = generateInstance(result, clazz);
		}
		return instance;
	}

	/**
	 * 
	 * @param entity
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void insert(final T entity) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();

		// generate insert query string
		String sql = generateInsertQuery(table, fields);
		LOG.debug("insert statement: " + sql);

		PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		// prepare values throught <?> references
		prepare(preparedStatement, fields, entity);

		preparedStatement.executeUpdate();

		ResultSet keys = preparedStatement.getGeneratedKeys();
		keys.next();
		@SuppressWarnings("unchecked")
		PK id = (PK) keys.getObject(1);
		set(fieldId, entity, id);
	}

	/**
	 * 
	 * @param entities
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public void insert(final List<T> entities) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();

		// generate insert query string
		String sql = generateInsertQuery(table, fields);
		LOG.debug("insert statement: " + sql);

		PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		for (T entity : entities) {
			// prepare values throught <?> references
			prepare(preparedStatement, fields, entity);
			// add to preparedstatement batch
			preparedStatement.addBatch();
		}

		preparedStatement.executeBatch();

		ResultSet keys = preparedStatement.getGeneratedKeys();
		List<PK> ids = new ArrayList<>();
		while (keys.next()) {
			ids.add((PK) keys.getObject(1));
		}
		for (int i = 0; i < entities.size(); i++) {
			set(fieldId, entities.get(i), ids.get(i));
		}
	}

	/**
	 * 
	 * @param entity
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void update(final T entity) throws SQLException {
		Field[] fields = clazz.getDeclaredFields();

		// generate update query string
		String sql = generateUpdateQuery(table, columnId, fields, entity);
		LOG.debug("update statement: " + sql);

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		// prepare values throught <?> references
		prepareUpdate(preparedStatement, fields, entity);

		preparedStatement.executeUpdate();

	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(PK id) throws SQLException {
		Statement statement = connection.createStatement();

		String sql = "DELETE FROM " + table.name() + " WHERE " + columnId.name() + " = " + id;
		LOG.debug("delete statement: " + sql);

		// execute delete based on id
		return statement.execute(sql);
	}

}
