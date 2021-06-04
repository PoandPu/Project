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

/**
 * Manipulates "subjects" table in the DB
 * 
 * @author O.Pavelchuk
 *
 */
public class SubjectDAO extends AbstractDAO {

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
	 * @param useJNDI
	 * @throws DBException
	 */
	private SubjectDAO(boolean useJNDI) throws DBException {
		super(useJNDI);
	}

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
	 * singleton pattern
	 * 
	 * @return instance of SubjectDAO
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
	 * @param useJNDI
	 * @return instance of SubjectDAO
	 * @throws DBException
	 */
	public static synchronized SubjectDAO getInstance(boolean useJNDI) throws DBException {
		if (instance == null) {
			instance = new SubjectDAO(useJNDI);
		}
		return instance;
	}

	/**
	 * Extracts a Subject object from ResultSet
	 * 
	 * @param ResultSet
	 * @return Subject
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
			pstmt = con.prepareStatement(SQL_FIND_SUBJECT_BY_NAME);
			int k = 1;
			pstmt.setString(k++, name);
			pstmt.setString(k, name);

			resultSet = pstmt.executeQuery();
			result = resultSet.next();
		} catch (SQLException e) {
			LOG.error("cannot check Subject name uniqueness in the DB");
			throw new DBException("cannot check Subject name uniqueness in the DB", e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return result;
	}

	/**
	 * Inserts subject in the DB
	 * 
	 * @param Subject
	 * @return boolean
	 * @throws DBException
	 */
	public boolean insert(Subject subject) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_SUBJECT, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, subject.getNameRu());
			pstmt.setString(k, subject.getNameEn());

			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					subject.setId(resultSet.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			LOG.error("cannot insert a subject");
			throw new DBException("cannot insert a subject", e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return result;
	}

	/**
	 * Update subject
	 * 
	 * @param Subject
	 * @return boolean
	 * @throws DBException
	 */
	public boolean update(Subject subject) throws DBException {
		boolean result = false;

		PreparedStatement pstmt = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, subject.getNameRu());
			pstmt.setString(k++, subject.getNameEn());
			pstmt.setInt(k, subject.getId());

			result = pstmt.executeUpdate() > 0;
			LOG.trace("Subject with (id: " + subject.getId() + ") was updated");
		} catch (SQLException e) {
			LOG.error("cannot upate a subject");
			throw new DBException("cannot upate a subject", e);
		} finally {
			close(con, pstmt);
		}
		return result;
	}

	/**
	 * Deletes subject by id
	 * 
	 * @param id
	 * @return boolean
	 * @throws DBException
	 */
	public boolean delete(int id) throws DBException {
		boolean result = false;

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_SUBJECT_BY_ID);
			pstmt.setInt(1, id);

			result = pstmt.executeUpdate() > 0;
			LOG.trace("Subject with (id: " + id + ") was deleted");
		} catch (SQLException e) {
			LOG.error("cannot delete a subject");
			throw new DBException("cannot delete a subject", e);
		} finally {
			close(con, pstmt);
		}
		return result;
	}

	/**
	 * Finds all subjects, paginated and sorted
	 * 
	 * @param orderBy
	 * @param direction
	 * @param offset
	 * @param lines
	 * @return List of subjects
	 * @throws DBException
	 */
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
			LOG.error("cannot find all sorted and paginated subjects");
			throw new DBException("cannot find all sorted and paginated subjects", e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return subjects;
	}

	/**
	 * Finds subject by id
	 * 
	 * @param id
	 * @return Subject
	 * @throws DBException
	 */
	public Subject findSubjectById(int id) throws DBException {
		Subject subject = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_GET_SUBJECT_BY_ID);
			pstmt.setInt(1, id);
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				subject = extract(resultSet);
			}
		} catch (SQLException e) {
			LOG.error("Cannot find subject by id");
			throw new DBException("Cannot find subject by id", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return subject;
	}
}
