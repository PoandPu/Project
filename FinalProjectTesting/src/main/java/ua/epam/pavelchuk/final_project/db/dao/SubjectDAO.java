package ua.epam.pavelchuk.final_project.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.db.Fields;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;

/**
 * DAO class for working with MySQL
 * 
 * @author
 *
 */
public class SubjectDAO extends AbstractDAO {

	// singleton variable
	private static SubjectDAO instance;

	private static final Logger LOG = Logger.getLogger(SubjectDAO.class);

	private static final String SQL_GET_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
	private static final String SQL_INSERT_SUBJECT = "INSERT INTO subjects (name_ru, name_en) VALUES (?, ?)";
	private static final String SQL_UPDATE_SUBJECT = "UPDATE subjects SET name_ru=?, name_en=? WHERE id=?";
	private static final String SQL_DELETE_SUBJECT_BY_ID = "DELETE FROM subjects WHERE id=?";
	private static final String SQL_FIND_SUBJECT_BY_NAME = "SELECT * FROM subjects WHERE name_ru=? or name_en=?";
	private static final String SQL_FIND_ALL_SUBJECT = "SELECT * FROM subjects";

	/**
	 * standard constructor
	 * 
	 * @throws DBException
	 */
	private SubjectDAO() throws DBException {
		super();
	}

	/**
	 * constructor constructor with the option not to use JNDI for Junit
	 * 
	 * @param isUseJNDI
	 * @throws DBException
	 */
	private SubjectDAO(boolean isUseJNDI) throws DBException {
		super(isUseJNDI);
	}

	/**
	 * Extract subject from ResultSet
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private Subject extract(ResultSet resultSet) throws SQLException {
		Subject subject = new Subject();
		subject.setId(resultSet.getInt(Fields.ENTITY_ID));
		subject.setNameEn(resultSet.getString(Fields.SUBJECTS_NAME_EN));
		subject.setNameRu(resultSet.getString(Fields.SUBJECTS_NAME_RU));
		return subject;
	}

	/**
	 * singleton pattern
	 * 
	 * @return
	 * @throws DBException
	 */
	public static synchronized SubjectDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new SubjectDAO();
		}
		return instance;
	}

	/**
	 * singleton pattern with use constructor with the option not to use JNDI for
	 * Junit
	 * 
	 * @param isUseJNDI
	 * @return
	 * @throws DBException
	 */
	public static synchronized SubjectDAO getInstance(boolean isUseJNDI) throws DBException {
		if (instance == null) {
			instance = new SubjectDAO(isUseJNDI);
		}
		return instance;
	}
	/**
	 * Check name
	 * 
	 * @param name
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean hasName(String name) throws DBException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_SUBJECT_BY_NAME);
			int k = 1;
			pstmt.setString(k++, name);
			pstmt.setString(k, name);
			resultSet = pstmt.executeQuery();
			result = resultSet.next();
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return result;
	}

	/**
	 * Insert subject
	 * 
	 * @param subject
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean insert(Subject subject) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;

		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_INSERT_SUBJECT, Statement.RETURN_GENERATED_KEYS);
			int k = 1;

			pstmt.setString(k++, subject.getNameRu());
			pstmt.setString(k, subject.getNameEn());

			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					con.commit();
					subject.setId(resultSet.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return result;
	}

	/**
	 * Update subject
	 * 
	 * @param subject
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean update(Subject subject) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;

		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_UPDATE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
			int k = 1;

			pstmt.setString(k++, subject.getNameRu());
			pstmt.setString(k++, subject.getNameEn());
			pstmt.setInt(k, subject.getId());

			if (pstmt.executeUpdate() > 0) {
				con.commit();
				LOG.trace("Subject with id " + subject.getId() + " was update");
			}
			result = true;
		} catch (SQLException e) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	/**
	 * Delete subject by id
	 * 
	 * @param id
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean delete(int id) throws DBException {
		boolean result = false;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_DELETE_SUBJECT_BY_ID);
			pstmt.setInt(1, id);

			result = pstmt.executeUpdate() > 0;
			con.commit();
			LOG.trace("Subject was delited (id: " + id + ")");
		} catch (SQLException e) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException();
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	public List<Subject> findAllOrderBy(String orderBy, String direction, int offset, int lines) throws DBException {

		List<Subject> subjects = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_SUBJECT + " ORDER BY " + orderBy + " " + direction + " LIMIT "
					+ offset + ", " + lines);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				subjects.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return subjects;
	}

	public Subject getSubjectById(int id) throws DBException {
		Subject subject = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_GET_SUBJECT_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				subject = extract(rs);
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find test by theme id", e);
		} finally {
			close(con, pstmt, rs);
		}
		return subject;
	}

}
