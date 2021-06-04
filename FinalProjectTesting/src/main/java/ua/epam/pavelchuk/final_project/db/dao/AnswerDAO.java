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
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

/**
 * Manipulates "answers" table in the DB
 * 
 * @author O.Pavelchuk
 *
 */
public class AnswerDAO extends AbstractDAO {

	/**
	 * standard constructor
	 * 
	 * @throws DBException
	 */
	private AnswerDAO() throws DBException {
		super();
	}
	
	/**
	 * constructor constructor with the option not to use JNDI for Junit
	 * 
	 * @param useJNDI
	 * @throws DBException
	 */
	private AnswerDAO(boolean useJNDI) throws DBException {
		super(useJNDI);
	}

	private static AnswerDAO instance;
	private static final Logger LOG = Logger.getLogger(AnswerDAO.class);

	private static final String GET_ANSWERS_BY_QUESTION_ID = "SELECT * FROM answers WHERE question_id = ?";
	private static final String GET_CORRECT_ANSWERS_BY_QUESTION = "SELECT * FROM answers WHERE question_id = ? AND isCorrect=1";
	private static final String SQL_DELETE_ANSWER_BY_ID = "DELETE FROM answers WHERE id=?";
	private static final String SQL_UPDATE_ANSWER_BY_ID = "UPDATE answers SET option_ru = ?, option_en = ?, isCorrect = ? WHERE id = ?";
	private static final String SQL_INSERT_ANSWER = "INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE (?, ?, ?, ?)";

	/**
	 * singleton
	 * 
	 * @return instance of AnswerDAO
	 * @throws DBException
	 */
	public static synchronized AnswerDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new AnswerDAO();
		}
		return instance;
	}

	/**
	 * singleton
	 * 
	 * @param useJNDI
	 * @return instance of AnswerDAO
	 * @throws DBException
	 */
	public static synchronized AnswerDAO getInstance(boolean useJNDI) throws DBException {
		if (instance == null) {
			instance = new AnswerDAO(useJNDI);
		}
		return instance;
	}
	
	/**
	 * Extracts an Answer object from ResultSet
	 * 
	 * @param ResultSet
	 * @return Answer
	 * @throws SQLException
	 */
	private Answer extract(ResultSet rs) throws SQLException {
		Answer answer = new Answer();
		answer.setId(rs.getInt(Fields.ID));
		answer.setNameRu(rs.getString(Fields.ANSWER_OPTION_RU));
		answer.setNameEn(rs.getString(Fields.ANSWER_OPTION_EN));
		answer.setIsCorrect(rs.getBoolean(Fields.ANSWER_CORRECT));
		return answer;
	}

	/**
	 * Finds all answers for question
	 * 
	 * @param question id
	 * @return List of answers
	 * @throws DBException
	 */
	public List<Answer> findAnswersByQuestion(int questionId) throws DBException {
		List<Answer> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_ANSWERS_BY_QUESTION_ID);
			pstmt.setInt(1, questionId);
			
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				list.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error("Cannot find answers by question id");
			throw new DBException("Cannot find answers by question id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return list;
	}

	/**
	 * Finds all correct answers for question
	 * 
	 * @param question id
	 * @return List of answers
	 * @throws DBException
	 */
	public List<Answer> findCorrectAnswersByQuestion(int questionId) throws DBException {
		List<Answer> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(GET_CORRECT_ANSWERS_BY_QUESTION);
			pstmt.setInt(1, questionId);
			
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				list.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error("Cannot find correct answers by question id");
			throw new DBException("Cannot find correct answers by question id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return list;
	}

	/**
	 * Deletes answer by id
	 * 
	 * @param id
	 * @return boolean
	 * @throws DBException
	 */
	public boolean delete(int id) throws DBException {
		boolean result = false;

		Connection con = null;
		PreparedStatement statement = null;

		try {
			con = getConnection();
			statement = con.prepareStatement(SQL_DELETE_ANSWER_BY_ID);
			statement.setInt(1, id);
			
			result = statement.executeUpdate() > 0;
			LOG.trace("Answer was deleted (id: " + id + ")");
		} catch (SQLException e) {
			LOG.error("Cannot delete answer");
			throw new DBException("Cannot delete answer", e);
		} finally {
			close(con, statement);
		}
		return result;
	}

	/**
	 * Update answer by id
	 * 
	 * @param id
	 * @return boolean
	 * @throws DBException
	 */
	public boolean update(Answer answer) throws DBException {
		boolean result = false;

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_ANSWER_BY_ID);
			int k = 1;
			pstmt.setString(k++, answer.getNameRu());
			pstmt.setString(k++, answer.getNameEn());
			pstmt.setBoolean(k++, answer.getIsCorrect());
			pstmt.setInt(k, answer.getId());
			
			if (pstmt.executeUpdate() > 0) {
				LOG.trace("Answer with id " + answer.getId() + " was updated");
				result = true;
			}
		} catch (SQLException e) {
			LOG.error("Cannot update answer");
			throw new DBException("Cannot update answer", e);
		} finally {
			close(con, pstmt);
		}
		return result;
	}

	/**
	 * Inserts answer to the DB
	 * 
	 * @param Answer
	 * @return boolean
	 * @throws DBException
	 */
	public boolean insert(Answer answer) throws DBException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_ANSWER, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, answer.getNameRu());
			pstmt.setString(k++, answer.getNameEn());
			pstmt.setInt(k++, answer.getQuestionId());
			pstmt.setBoolean(k++, answer.getIsCorrect());
			
			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					answer.setId(resultSet.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			LOG.error("Cannot insert answer");
			throw new DBException("Cannot insert answer", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}
}
