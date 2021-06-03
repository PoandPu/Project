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
import ua.epam.pavelchuk.final_project.db.entity.Result;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;

/**
 * Manipulates "results" table in the DB
 * 
 * @author O.Pavelchuk
 *
 */
public class ResultDAO extends AbstractDAO {
	
	/**
	 * standard constructor
	 * 
	 * @throws DBException
	 */
	private ResultDAO() throws DBException {
		super();
	}

	/**
	 * constructor constructor with the option not to use JNDI for Junit
	 * 
	 * @param useJNDI
	 * @throws DBException
	 */
	private ResultDAO(boolean isUseJNDI) throws DBException {
		super(isUseJNDI);
	}
	
	private static ResultDAO instance;
	private static final Logger LOG = Logger.getLogger(ResultDAO.class);
	
	private static final String SQL_INSERT_RESULT = "INSERT INTO results (mark, user_id, test_id) VALUES (?, ?, ?)";
	private static final String SQL_GET_RESULTS = "SELECT results.mark, results.test_date, tests.name_Ru, tests.name_En, subjects.name_Ru, subjects.name_En FROM tests "
			+ "JOIN results ON tests.id = results.test_id "
			+ "JOIN subjects ON tests.subject_id = subjects.id "
			+ "WHERE results.user_id = ?";
	private static final String SQL_DELETE_RESULT = "DELETE FROM results WHERE id = ?";
	

	/**
	 * singleton pattern
	 * 
	 * @return instance of ResultsDAO
	 * @throws DBException
	 */
	public static synchronized ResultDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new ResultDAO();
		}
		return instance;
	}
	
	/**
	 * singleton pattern
	 * 
	 * @return instance of ResultsDAO
	 * @throws DBException
	 */
	public static synchronized ResultDAO getInstance(boolean useJNDI) throws DBException {
		if (instance == null) {
			instance = new ResultDAO(useJNDI);
		}
		return instance;
	}
	
	/**
	 * Extracts a Result object from ResultSet
	 * 
	 * @param ResultSet
	 * @return Result
	 * @throws SQLException
	 */
	private Result extract(ResultSet rs) throws SQLException {
		Result result = new Result();
		result.setMark(rs.getBigDecimal(Fields.RESULT_MARK));
		result.setTestDate(rs.getTimestamp(Fields.RESULT_TEST_DATE).toString());
		result.setTestNameRu(rs.getString(Fields.TEST_NAME_RU));
		result.setTestNameEn(rs.getString(Fields.TEST_NAME_EN));
		result.setSubjectNameRu(rs.getString(Fields.SUBJECTS_NAME_RU));
		result.setSubjectNameEn(rs.getString(Fields.SUBJECTS_NAME_EN));
		return result;
	}
	
	/**
	 * Inserts result in the DB
	 * 
	 * @param Result
	 * @return boolean
	 * @throws DBException
	 */
	public boolean insert(Result result) throws DBException {
		boolean res = false;
		
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_RESULT, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setBigDecimal(k++, result.getMark());
			pstmt.setInt(k++, result.getEntrantId());
			pstmt.setInt(k++, result.getTestId());

			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					result.setId(resultSet.getInt(1));
				}
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return res;
	}
	
	/**
	 * Finds results by user ID
	 * 
	 * @param Result
	 * @return List
	 * @throws DBException
	 */
	public List<Result> findResultsByUserId(int userId) throws DBException {
		List<Result> results = new ArrayList<>();
		
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		Connection connection = null;
		
		try {
			connection = getConnection();
			pstm = connection.prepareStatement(SQL_GET_RESULTS);
			pstm.setInt(1, userId);
			
			resultSet = pstm.executeQuery();
				while (resultSet.next()) {
					results.add(extract(resultSet));	
				}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(connection, pstm, resultSet);
		}
		return results;
	}
	
	/**
	 * Finds results by user ID, paginated and sorted
	 * 
	 * @param userId 
	 * @param orderBy
	 * @param direction
	 * @param offset
	 * @param lines
	 * @return List of results
	 * @throws DBException
	 */
	public List<Result> findResultsByUserIdAllOrderedBy(int userId, String orderBy, String direction, int offset, int lines) throws DBException {
		List<Result> results = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection connection = null;
		
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(SQL_GET_RESULTS + " ORDER BY " + orderBy + " " + direction + " LIMIT " + offset + ", " + lines);
			pstmt.setInt(1, userId);
			
			resultSet = pstmt.executeQuery();
				while (resultSet.next()) {
					results.add(extract(resultSet));	
				}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(connection, pstmt, resultSet);
		}
		return results;
	}
	
	/**
	 * Deletes result by ID
	 * 
	 * @param result ID
	 * @return boolean
	 * @throws DBException
	 */
	public boolean delete(int id) throws DBException {
		boolean result = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_RESULT);
			pstmt.setInt(1, id);
			
			result = pstmt.executeUpdate() > 0;
			LOG.trace("Result was deleted (id: " + id + ")");
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException();
		} finally {
			close(con, pstmt);
		}
		return result;
	}
}
