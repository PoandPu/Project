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

/**
 * Manipulates "tests" table in the DB
 * 
 * @author O.Pavelchuk
 *
 */
public class TestDAO extends AbstractDAO {

	/**
	 * standard constructor
	 * 
	 * @throws DBException
	 */
	private TestDAO() throws DBException {
		super();
	}

	/**
	 * constructor constructor with the option not to use JNDI for Junit
	 * 
	 * @param useJNDI
	 * @throws DBException
	 */
	private TestDAO(boolean useJNDI) throws DBException {
		super(useJNDI);
	}

	private static final Logger LOG = Logger.getLogger(TestDAO.class);
	private static TestDAO instance;

	private static final String SQL_INSERT_TEST ="INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE (?, ?, ?, ?, ?);";
	private static final String SQL_GET_TEST_BY_ID ="SELECT * FROM tests WHERE id = ?";
	private static final String GET_TESTS_BY_SUBJECT = "SELECT * FROM tests WHERE subject_id = ?";
	private static final String GET_DIFFICULTY_BY_ID = "SELECT * FROM difficulty_level WHERE id = ?";
	private static final String GET_TIME_BY_ID = "SELECT time_minutes FROM tests Where id=?";
	private static final String INCREASE_REQUESTS_BY_ID = "UPDATE tests SET numb_of_requests = numb_of_requests + 1 WHERE id = ?";
	private static final String SQL_UPDATE_TEST ="UPDATE tests SET name_ru=?, name_en=?, time_minutes = ?, difficulty_level_id = ? WHERE id=?";
	private static final String SQL_DELETE_TEST_BY_ID ="DELETE FROM tests WHERE id=?";
	private static final String SQL_FIND_TEST_BY_NAME ="SELECT * FROM tests WHERE name_ru=? or name_en=?";
	
	/**
	 * singleton pattern
	 * 
	 * @return instance of TestDAO
	 * @throws DBException
	 */
	public static synchronized TestDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new TestDAO();
		}
		return instance;
	}
	
	/**
	 * singleton pattern that use constructor with the option not to use JNDI for
	 * Junit
	 * 
	 * @return instance of TestDAO
	 * @throws DBException
	 */
	public static synchronized TestDAO getInstance(boolean isUseJNDI) throws DBException {
		if (instance == null) {
			instance = new TestDAO(isUseJNDI);
		}
		return instance;
	}
	
	/**
	 * Extracts a Test object from ResultSet
	 * 
	 * @param ResultSet
	 * @return Test
	 * @throws SQLException
	 */
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
	
	/**
	 * Inserts Test in the DB
	 * 
	 * @param Test
	 * @return boolean
	 * @throws DBException
	 */
	public boolean insert(Test test)  throws DBException{
		boolean result = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
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
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					test.setId(resultSet.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			LOG.error("Cannot insert a test");
			throw new DBException("Cannot insert a test", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	/**
	 * Finds Test by subject ID, paginated and sorted
	 * 
	 * @param subject ID
	 * @param orderBy
	 * @param direction
	 * @param offset
	 * @param lines
	 * @return List of tests
	 * @throws DBException
	 */	
	public List<Test> findTestBySubjectIdAllOrderBy(int subjectId, String orderBy, String direction, int offset, int lines) throws DBException {
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
			LOG.error("Cannot find sorted and paginated list of tests by subject id");
			throw new DBException("Cannot find sorted and paginated list of tests by subject id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return tests;
	}
	
	/**
	 * Finds difficulty for test in Russian and English by difficulty ID
	 * 
	 * @param difficulty ID
	 * @return List of difficulty name in English and Russian
	 * @throws DBException
	 */	
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
			LOG.error("cannot find difficulty for test");
			throw new DBException("cannot find difficulty for test", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return difficultyName;
	}
		
	/**
	 * Finds time for test 
	 * 
	 * @param test ID
	 * @return int minutes
	 * @throws DBException
	 */	
	public int findTimeByTestId(int testId) throws DBException {
		int time = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_TIME_BY_ID);
			pstmt.setInt(1, testId);
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				time = resultSet.getInt(Fields.TEST_TIME_MINUTES); 
			}
		} catch (SQLException e) {
			LOG.error("cannot find time for test");
			throw new DBException("cannot find time for test", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return time;
	}
	
	/**
	 * Increases number of requests for test
	 * 
	 * @param test ID
	 * @return boolean
	 * @throws DBException
	 */	
	public boolean increaseRequests(int testId) throws DBException {
		boolean res = false;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(INCREASE_REQUESTS_BY_ID, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, testId);
			if(pstmt.executeUpdate() > 0) {
				LOG.trace("Number of requests of testId = " + testId + " was increased");
			}
			res = true;
		} catch (SQLException e) {
			LOG.error("cannot increase number of requests for test");
			throw new DBException("cannot increase number of requests for test", e);
		} finally {
			close(con, pstmt);
		}
		return res;
	}
	
	/**
	 * Finds test in the DB by ID
	 * 
	 * @param test ID
	 * @return Test
	 * @throws DBException
	 */	
	public Test findTestById(int id) throws DBException {
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
			LOG.error("Cannot find test by id");
			throw new DBException("Cannot find test by id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return test;
	}
	
	/**
	 * Updates test
	 * @param test
	 * @return boolean
	 * @throws DBException
	 */
	public boolean update(Test test) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		
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
				result = true;
				LOG.trace("Subject with id " + test.getId() + " was updated");
			}
		} catch (SQLException e) {
			LOG.error("Failed to update test");
			throw new DBException("Failed to update test", e);
		} finally {
			close(con, pstmt);
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
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_TEST_BY_ID);
			pstmt.setInt(1, id);
			
			result =  pstmt.executeUpdate() > 0;
			LOG.trace("Subject was delited (id: " + id + ")");
		} catch (SQLException e) {
			LOG.error("Failed to update test");
			throw new DBException("Failed to update test", e);
		} finally {
			close(con, pstmt);
		}
		return result;
	}
	
	/**
	 * Checks if a name exists in the DB
	 * 
	 * @param name
	 * @return boolean
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
			pstmt.setString(k++, name);
			
			resultSet = pstmt.executeQuery();
			result = resultSet.next();
		} catch (SQLException e) {
			LOG.error("Cannot check name uniqueness of the test");
			throw new DBException("Cannot check name uniqueness of the test", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}
}
