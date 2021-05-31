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
import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;

public class TestDAO extends AbstractDAO {

	private TestDAO() throws DBException {
		super();
	}

	private TestDAO(boolean isUseJNDI) throws DBException {
		super(isUseJNDI);
	}

	private static final Logger LOG = Logger.getLogger(TestDAO.class);

	private static TestDAO instance;

	/**
	 * singleton pattern
	 * 
	 * @return
	 * @throws DBException
	 */
	public static synchronized TestDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new TestDAO();
		}
		return instance;
	}
	
	/**
	 * singleton pattern
	 * 
	 * @return
	 * @throws DBException
	 */
	public static synchronized TestDAO getInstance(boolean isUseJNDI) throws DBException {
		if (instance == null) {
			instance = new TestDAO(isUseJNDI);
		}
		return instance;
	}
	
	private static final String SQL_INSERT_TEST ="INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE (?, ?, ?, ?, ?);";
	private static final String SQL_GET_TEST_BY_ID ="SELECT * FROM tests WHERE id = ?";
	private static final String GET_TESTS_BY_SUBJECT = "SELECT * FROM tests WHERE subject_id = ?";
	private static final String GET_DIFFICULTY_BY_ID = "SELECT * FROM difficulty_level WHERE id = ?";
	private static final String GET_TIME_BY_ID = "SELECT time_minutes FROM tests Where id=?";
	private static final String INCREASE_REQUESTS_BY_ID = "UPDATE tests SET numb_of_requests = numb_of_requests + 1 WHERE id = ?";
	private static final String SQL_UPDATE_TEST ="UPDATE tests SET name_ru=?, name_en=?, time_minutes = ?, difficulty_level_id = ? WHERE id=?";
	private static final String SQL_DELETE_TEST_BY_ID ="DELETE FROM tests WHERE id=?";
	private static final String SQL_FIND_TEST_BY_NAME ="SELECT * FROM tests WHERE name_ru=? or name_en=?";
	
	public boolean insert(Test test)  throws DBException{
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_TEST, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, test.getNameRu());
			pstmt.setString(k++, test.getNameEn());
			pstmt.setInt(k++, test.getSubjectId());
			pstmt.setInt(k++, test.getDifficultyLevel());
			pstmt.setInt(k++, test.getTime());
			if(pstmt.executeUpdate()>0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					test.setId(rs.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find test by theme id", e);
		} finally {
			close(con, pstmt, rs);
		}
		return result;
	}
	
	
	public List<Test> findTestsBySubject(int subjectId) throws DBException {
		List<Test> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_TESTS_BY_SUBJECT);
			pstmt.setInt(1, subjectId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				list.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find test by theme id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return list;
	}

	private Test extract(ResultSet rs) throws SQLException, DBException {
		Test test = new Test();
		test.setId(rs.getInt(Fields.ID));
		test.setNameRu(rs.getString(Fields.TEST_NAME_RU));
		test.setNameEn(rs.getString(Fields.TEST_NAME_EN));
		test.setDifficultyLevel(rs.getInt(Fields.DIFFICULTY_LEVEL_ID));
		List<String> difficultyNames = findDifficultyForTest(test.getDifficultyLevel());
		test.setDifficultyNameEn(difficultyNames.get(0));
		test.setDifficultyNameRu(difficultyNames.get(1));
		test.setTime(rs.getInt(Fields.TEST_TIME_MINUTES));
		test.setSubjectId(rs.getInt(Fields.SUBJECT_ID));
		test.setRequests(rs.getInt(Fields.TEST_REQ_NUMB));
		return test;
	}

	public List<Test> findAllOrderBy(String orderBy, String direction, int subjectId, int offset, int lines) throws DBException {
		List<Test> tests = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_TESTS_BY_SUBJECT + " ORDER BY " + orderBy + " " + direction +" LIMIT " + offset + ", " + lines );
			pstmt.setInt(1, subjectId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				tests.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return tests;
	}
	
	
	
	private List<String> findDifficultyForTest(int difficultyId) throws DBException {
		List<String> difficultyName = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_DIFFICULTY_BY_ID);
			pstmt.setInt(1, difficultyId);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				difficultyName.add(resultSet.getString(Fields.DIFFICULTY_NAME_EN));
				difficultyName.add(resultSet.getString(Fields.DIFFICULTY_NAME_RU));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return difficultyName;
	}
		
	public int findTimeByTestId(int testId) throws DBException {
		int time = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_TIME_BY_ID );
			pstmt.setInt(1, testId);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				time = resultSet.getInt(Fields.TEST_TIME_MINUTES); 
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find test by theme id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return time;
	}
	
	public boolean increaseRequests(int testId) throws DBException {
		boolean res = false;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection con = null;
		
		try {
			con = getConnection();
			statement = con.prepareStatement(INCREASE_REQUESTS_BY_ID, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, testId);
			if(statement.executeUpdate() > 0) {
				LOG.trace("Number of requests of testId = " + testId + " was increased");
			}
			res = true;
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, statement, resultSet);
		}
		return res;
	}
	
	public Test getTestById(int id) throws DBException {
		Test test = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_GET_TEST_BY_ID);
			pstmt.setInt(1, id);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				test = extract(resultSet); 
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find test by theme id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return test;
	}
	
	
	/**
	 * Update test
	 * @param test
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean update(Test test) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;
		
		try {
			
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_TEST, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			
			pstmt.setString(k++, test.getNameRu());
			pstmt.setString(k++, test.getNameEn());
			pstmt.setInt(k++, test.getTime());
			pstmt.setInt(k++, test.getDifficultyLevel());
			pstmt.setInt(k, test.getId());
			
			if(pstmt.executeUpdate() > 0) {
				LOG.trace("Subject with id " + test.getId() + " was updated");
			}
			result = true;
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}
	
	/**
	 * Delete test by id
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
			pstmt = con.prepareStatement(SQL_DELETE_TEST_BY_ID);
			pstmt.setInt(1, id);
			
			result =  pstmt.executeUpdate() > 0;
			LOG.trace("Subject was delited (id: " + id + ")");
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException();
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}
	
	/**
	 * Check name
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
			pstmt = con.prepareStatement(SQL_FIND_TEST_BY_NAME);
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
	
	

}
