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
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;

public class QuestionDAO extends AbstractDAO {

	private QuestionDAO() throws DBException {
		super();
	}

	private QuestionDAO(boolean isUseJNDI) throws DBException {
		super(isUseJNDI);
	}

	private static QuestionDAO instance;
	private static final Logger LOG = Logger.getLogger(QuestionDAO.class);
	private static final String GET_QUESTIONS_BY_TEST = "SELECT * FROM questions WHERE test_id = ?";
	private static final String GET_QUESTION_BY_ID = "SELECT * FROM questions WHERE id = ?";

	private static final String SQL_DELETE_QUESTION_BY_ID = "DELETE FROM questions WHERE id=?";

	private static final String SQL_UPDATE_QUESTION_BY_ID = "UPDATE questions SET title_ru = ?, title_en = ? WHERE id = ?";
	private static final String SQL_INSERT_QUESTION = "INSERT INTO questions (title_ru, title_en, test_id) VALUE (?, ?, ?)";

	/**
	 * singleton pattern
	 * 
	 * @return
	 * @throws DBException
	 */
	public static synchronized QuestionDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new QuestionDAO();
		}
		return instance;
	}
	
	/**
	 * singleton pattern
	 * 
	 * @return
	 * @throws DBException
	 */
	public static synchronized QuestionDAO getInstance(boolean useJNDI) throws DBException {
		if (instance == null) {
			instance = new QuestionDAO(useJNDI);
		}
		return instance;
	}

	public List<Question> findQuestionsByTest(int testId) throws DBException {
		List<Question> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(GET_QUESTIONS_BY_TEST);
			pstmt.setInt(1, testId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				list.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find questions by test id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return list;
	}

	public Question findQuestionById(int id) throws DBException {
		Question question = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(GET_QUESTION_BY_ID);
			pstmt.setInt(1, id);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				question = extract(resultSet);
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find question by id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return question;
	}

	private Question extract(ResultSet rs) throws SQLException {
		Question question = new Question();
		question.setId(rs.getInt(Fields.ID));
		question.setNameRu(rs.getString(Fields.QUESTION_TITLE_RU));
		question.setNameEn(rs.getString(Fields.QUESTION_TITLE_EN));
		return question;
	}

	/**
	 * Delete question by id
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
			pstmt = con.prepareStatement(SQL_DELETE_QUESTION_BY_ID);
			pstmt.setInt(1, id);

			result = pstmt.executeUpdate() > 0;
			LOG.trace("Questions was deleted (id: " + id + ")");
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException();
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	/**
	 * Delete question by id
	 * 
	 * @param id
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean update(Question question) throws DBException {
		boolean result = false;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_QUESTION_BY_ID);
			int k = 1;
			pstmt.setString(k++, question.getNameRu());
			pstmt.setString(k++, question.getNameEn());
			pstmt.setInt(k, question.getId());

			if (pstmt.executeUpdate() > 0) {
				LOG.trace("Question with id " + question.getId() + " was updated");
				result = true;
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException();
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	public boolean insert(Question qiestion) throws DBException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, qiestion.getNameRu());
			pstmt.setString(k++, qiestion.getNameEn());
			pstmt.setInt(k++, qiestion.getTestId());
			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					qiestion.setId(resultSet.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException("Cannot find test by theme id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

}
