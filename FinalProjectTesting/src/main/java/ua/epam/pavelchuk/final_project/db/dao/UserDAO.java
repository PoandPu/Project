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
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;

/**
 * DAO class for working with MySQL
 * 
 * @author
 *
 */
public class UserDAO extends AbstractDAO {

	private static final Logger LOG = Logger.getLogger(UserDAO.class);

	private static UserDAO instance;

	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
	private static final String SQL_INSERT_USER = "INSERT INTO users (login, password, `password_key`, language, first_name, last_name, email, role_id)"
			+ " VALUES (?,?,?,?,?,?,?,?)";
	private static final String SQL_GET_USER_ID_BY_LOGIN = "SELECT id FROM users WHERE login=?";
	private static final String SQL_GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
	private static final String SQL_UPDATE_USER = "UPDATE users SET "
			+ "password =?, language=?, role_id = ?, first_name=?, last_name=?, email=?" + "WHERE id=?";
	private static final String SQL_GET_USERS = "SELECT * FROM users";
	private static final String SQL_SEARCH_USERS = "SELECT users.* FROM users JOIN roles ON users.role_id = roles.id "
			+ "WHERE first_name LIKE ? " + "OR last_name LIKE ? " + "OR email LIKE ? " + "OR login LIKE ? "
			+ "OR roles.name LIKE ? " + "OR users.id LIKE ?";
	private static final String SQL_BLOCK_USER_BY_ID = "UPDATE users SET isBlocked = 1 WHERE id=?";
	private static final String SQL_UNBLOCK_USER_BY_ID = "UPDATE users SET isBlocked = 0 WHERE id=?";
	private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";
	
	private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

	/**
	 * constructor constructor with the option not to use JNDI for Junit
	 * 
	 * @param isUseJNDI
	 * @throws DBException
	 */
	private UserDAO(boolean isUseJNDI) throws DBException {
		super(isUseJNDI);
	}

	/**
	 * standard constructor
	 * 
	 * @throws DBException
	 */
	private UserDAO() throws DBException {
		super();
	}

	/**
	 * Extract user from ResultSet
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private User extract(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt(Fields.ENTITY_ID));
		user.setLanguage(rs.getString(Fields.USER_LANGUAGE));
		user.setLogin(rs.getString(Fields.USER_LOGIN));
		user.setPassword(rs.getString(Fields.USER_PASSWORD));
		user.setPasswordKey(rs.getString(Fields.USER_PASSWORD_KEY));
		user.setRoleId(rs.getInt(Fields.USER_ROLE));
		user.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
		user.setLastName(rs.getString(Fields.USER_LAST_NAME));
		user.setEmail(rs.getString(Fields.USER_EMAIL));
		user.setIsBlocked(rs.getBoolean(Fields.USER_IS_BLOCKED));
		return user;
	}

	/**
	 * singleton pattern
	 * 
	 * @return
	 * @throws DBException
	 */
	public static synchronized UserDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new UserDAO();
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
	public static synchronized UserDAO getInstance(boolean isUseJNDI) throws DBException {
		if (instance == null) {
			instance = new UserDAO(isUseJNDI);
		}
		return instance;
	}

	/**
	 * Return user id by login
	 * 
	 * @param login
	 * @return user id or -1 if user with this login isn't exist
	 * @throws DBException
	 */
	public int getIdByLogin(String login) throws DBException {
		int result = -1;

		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_GET_USER_ID_BY_LOGIN);
			pstmt.setString(1, login);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt(Fields.ENTITY_ID);
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return result;
	}

	/**
	 * Find user by id
	 * 
	 * @param id
	 * @return User
	 * @throws DBException
	 */
	public User findById(int id) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_GET_USER_BY_ID);
			pstmt.setInt(1, id);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				user = extract(resultSet);
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return user;
	}

	/**
	 * Find user by login
	 * 
	 * @param login
	 * @return User
	 * @throws DBException
	 */
	public User findByLogin(String login) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				user = extract(resultSet);
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return user;
	}

	/**
	 * Insert user
	 * 
	 * @param user
	 * @return User
	 * @throws DBException
	 */
	public boolean insert(User user) throws DBException {

		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;
		if (user == null) {
			return result;
		}

		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			System.out.println(SQL_INSERT_USER);
			pstmt.setString(k++, user.getLogin());
			System.out.println(user.getLogin());
			
			pstmt.setString(k++, user.getPassword());
			System.out.println(user.getPassword());
			
			pstmt.setString(k++, user.getPasswordKey());
			System.out.println(user.getPasswordKey());
			
			pstmt.setString(k++, user.getLanguage());
			System.out.println(user.getLanguage());
			
			pstmt.setString(k++, user.getFirstName());
			System.out.println(user.getFirstName());
			
			pstmt.setString(k++, user.getLastName());
			System.out.println(user.getLastName());
			
			pstmt.setString(k++, user.getEmail());
			System.out.println(user.getEmail());
			
			pstmt.setInt(k++, user.getRoleId());
			System.out.println(user.getRoleId());

			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					con.commit();
					user.setId(resultSet.getInt(1));
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
	 * Check login
	 * 
	 * @param login
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean hasLogin(String login) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);

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
	 * Update
	 * 
	 * @param user
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean update(User user) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;

		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_UPDATE_USER, Statement.RETURN_GENERATED_KEYS);
			int k = 1;

			pstmt.setString(k++, user.getPassword());
			pstmt.setString(k++, user.getLanguage());
			pstmt.setInt(k++, user.getRoleId());
			pstmt.setString(k++, user.getFirstName());
			pstmt.setString(k++, user.getLastName());
			pstmt.setString(k++, user.getEmail());
			pstmt.setInt(k, user.getId());

			if (pstmt.executeUpdate() > 0) {
				LOG.trace("User with login " + user.getLogin() + " was update");
				result = true;
				con.commit();
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
	 * This method returns a list of entrants with pagination and ordering
	 * 
	 * @param offset
	 * @param lines
	 * @return list of entrants
	 * @throws DBException
	 */
	public List<User> findAllUsersOrderBy(String orderBy, String direction, int offset, int lines) throws DBException {
		List<User> users = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(
					SQL_GET_USERS + " ORDER BY " + orderBy + " " + direction + " LIMIT " + offset + ", " + lines);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				users.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return users;
	}

	/**
	 * Check email
	 * 
	 * @param email
	 * @return result true or false
	 * @throws DBException
	 */
	public boolean hasEmail(String email) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		boolean result = false;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL);
			pstmt.setString(1, email);
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
	 * Block entrant by id
	 * 
	 * @param id
	 * @throws DBException
	 */
	public boolean blockById(int id) throws DBException {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_BLOCK_USER_BY_ID);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate() > 0;
			con.commit();
			LOG.trace("User (id:" + id + ") has been blocked ");
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
	 * Unblock for entrant by id
	 * 
	 * @param id
	 * @throws DBException
	 */
	public boolean unblockById(int id) throws DBException {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_UNBLOCK_USER_BY_ID);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate() > 0;
			con.commit();
			LOG.trace("User (id: " + id + ") has been unblocked ");
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
	 * This method returns a list of users with pagination and ordering
	 * 
	 * @param offset
	 * @param lines
	 * @return list of users
	 * @throws DBException
	 */
	public List<User> findUsersOrderBy(String pattern) throws DBException {
		List<User> users = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		pattern = "%" + pattern + "%";
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_SEARCH_USERS);
			int k = 1;
			pstmt.setString(k++, pattern);
			pstmt.setString(k++, pattern);
			pstmt.setString(k++, pattern);
			pstmt.setString(k++, pattern);
			pstmt.setString(k++, pattern);
			pstmt.setString(k++, pattern);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				users.add(extract(resultSet));
			}
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return users;
	}
	
	/**
	 * Find user by id
	 * 
	 * @param id
	 * @return User
	 * @throws DBException
	 */
	public boolean deleteById(int id) throws DBException {
		boolean result = false;
		PreparedStatement pstmt = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_USER_BY_ID);
			pstmt.setInt(1, id);

			result = pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		} finally {
			close(con);
			close(pstmt);
		}
		return result;
	}

}
