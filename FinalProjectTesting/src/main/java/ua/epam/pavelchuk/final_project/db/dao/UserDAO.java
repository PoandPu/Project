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
import ua.epam.pavelchuk.final_project.web.password_encryption.PasswordUtils;

/**
 * Manipulates "users" table in the DB
 * 
 * @author O.Pavelchuk
 *
 */
public class UserDAO extends AbstractDAO {
	
	/**
	 * constructor constructor with the option not to use JNDI for Junit
	 * 
	 * @param useJNDI
	 * @throws DBException
	 */
	private UserDAO(boolean useJNDI) throws DBException {
		super(useJNDI);
	}

	/**
	 * standard constructor
	 * 
	 * @throws DBException
	 */
	private UserDAO() throws DBException {
		super();
	}

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

	private static final String SQL_FIND_USER_LOGIN_MAIL = "SELECT * FROM users WHERE email LIKE ? OR login LIKE ?";
	private static final String SQL_INSERT_HASH = "INSERT INTO pass_recovery(hash, user_id) VALUE (?,?);";
	
	private static final String SQL_CREATE_EVENT = "CREATE EVENT IF NOT EXISTS ";
	private static final String SQL_EVENT_PROPERTIES = " ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 10 MINUTE DO DELETE FROM pass_recovery WHERE `hash` = ?";
	
	private static final String SQL_FIND_USER_BY_HASH = "SELECT users.* FROM pass_recovery JOIN users ON users.id = pass_recovery.user_id WHERE pass_recovery.hash = ?";		
	private static final String SQL_DELETE_HASH = "DELETE FROM pass_recovery WHERE `hash` = ?";
	
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
	 * @param useJNDI
	 * @return
	 * @throws DBException
	 */
	public static synchronized UserDAO getInstance(boolean useJNDI) throws DBException {
		if (instance == null) {
			instance = new UserDAO(useJNDI);
		}
		return instance;
	}
	
	/**
	 * Extracts user from ResultSet
	 * 
	 * @param ResultSet
	 * @return User
	 * @throws SQLException
	 */
	private User extract(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt(Fields.ID));
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
				result = resultSet.getInt(Fields.ID);
			}
		} catch (SQLException e) {
			LOG.error("cannot get user ID by login");
			throw new DBException("cannot get user ID by login", e);
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
			LOG.error("Cannot find user by id");
			throw new DBException("Cannot find user by id", e);
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
			LOG.error("Cannot find user by login", e);
			throw new DBException("Cannot find user by login", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return user;
	}

	/**
	 * Inserts user int the DB
	 * 
	 * @param User
	 * @return boolean
	 * @throws DBException
	 */
	public boolean insert(User user) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		
		if (user == null) {
			return result;
		}

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, user.getLogin());
			pstmt.setString(k++, user.getPassword());
			pstmt.setString(k++, user.getPasswordKey());
			pstmt.setString(k++, user.getLanguage());
			pstmt.setString(k++, user.getFirstName());
			pstmt.setString(k++, user.getLastName());
			pstmt.setString(k++, user.getEmail());
			pstmt.setInt(k++, user.getRoleId());
			
			if (pstmt.executeUpdate() > 0) {
				resultSet = pstmt.getGeneratedKeys();
				if (resultSet.next()) {
					user.setId(resultSet.getInt(1));
					result = true;
				}
			}
		} catch (SQLException e) {
			LOG.error("Cannot insert user");
			throw new DBException("Cannot insert user", e);
		} finally {
			close(con, pstmt, resultSet);
		}

		return result;
	}

	/**
	 * Checks if login already exists in the DB
	 * 
	 * @param login
	 * @return boolean
	 * @throws DBException
	 */
	public boolean hasLogin(String login) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);

			resultSet = pstmt.executeQuery();
			result = resultSet.next();
		} catch (SQLException e) {
			LOG.error("Cannot check login uniqueness");
			throw new DBException("Cannot check login uniqueness", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	/**
	 * Updates user
	 * 
	 * @param user
	 * @return boolean
	 * @throws DBException
	 */
	public boolean update(User user) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		Connection con = null;

		try {
			con = getConnection();
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
			}
		} catch (SQLException e) {
			LOG.error("cannot update a user");
			throw new DBException("cannot update a user", e);
		} finally {
			close(con, pstmt);
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
			LOG.error("cannot get users list");
			throw new DBException("cannot get users list", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return users;
	}

	/**
	 * Checks if email already exists in the DB
	 * 
	 * @param email
	 * @return boolean
	 * @throws DBException
	 */
	public boolean hasEmail(String email) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL);
			pstmt.setString(1, email);
			
			resultSet = pstmt.executeQuery();
			result = resultSet.next();
		} catch (SQLException e) {
			LOG.error("cannot chech email uniqueness");
			throw new DBException("cannot chech email uniqueness", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return result;
	}

	/**
	 * Blocks user by id
	 * 
	 * @param id
	 * @throws DBException
	 */
	public boolean blockById(int id) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_BLOCK_USER_BY_ID);
			pstmt.setInt(1, id);
			
			result = pstmt.executeUpdate() > 0;
			LOG.trace("User (id:" + id + ") has been blocked ");
		} catch (SQLException e) {
			LOG.error("Failed to block user");
			throw new DBException("Failed to block user", e);
		} finally {
			close(con, pstmt);
		}
		return result;
	}

	/**
	 * Unblocks user by id
	 * 
	 * @param id
	 * @throws DBException
	 */
	public boolean unblockById(int id) throws DBException {
		boolean result = false;
		
		PreparedStatement pstmt = null;
		Connection con = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UNBLOCK_USER_BY_ID);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate() > 0;
			LOG.trace("User (id: " + id + ") has been unblocked ");
		} catch (SQLException e) {
			LOG.error("Failed to unblock user");
			throw new DBException("Failed to unblock user", e);
		} finally {
			close(con, pstmt);
		}
		return result;
	}

	/**
	 * This method returns a list of users founded by pattern, paginated and sorted
	 * 
	 * @param pattern
	 * @return list of users
	 * @throws DBException
	 */
	public List<User> findUsersLike(String pattern) throws DBException {
		List<User> users = new ArrayList<>();
		pattern = "%" + pattern + "%";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
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
			LOG.error("Failed to find user by pattern");
			throw new DBException("Failed to find user by pattern", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return users;
	}
	
	/**
	 * This method returns a list of users founded by pattern (search users only by login or email), paginated and sorted
	 * 
	 * @param pattern
	 * @return list of users
	 * @throws DBException
	 */
	public User findUserByLoginMail(String pattern) throws DBException {
		User user = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_LOGIN_MAIL);
			int k = 1;
			pstmt.setString(k++, pattern);
			pstmt.setString(k++, pattern);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				user = extract(resultSet);
			}
		} catch (SQLException e) {
			LOG.error("Failed to find user by pattern for password recovery");
			throw new DBException("Failed to find user by pattern for password recovery", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return user;
	}
	
	/**
	 * Deletes user by id
	 * 
	 * @param id
	 * @return boolean
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
			LOG.error("Failed to delete user");
			throw new DBException("Failed to delete user", e);
		} finally {
			close(con);
			close(pstmt);
		}
		return result;
	}
	
	
	/**
	 * Creates a password recovery link
	 * 
	 * @param User
	 * @return String hash
	 * @throws DBException
	 */
	public String createHash(User user) throws DBException {
		String hash = PasswordUtils.getSalt(45);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_INSERT_HASH);
			int k = 1;
			pstmt.setString(k++, hash);
			pstmt.setInt(k++, user.getId());
			pstmt.executeUpdate();
			close(pstmt);
			
			pstmt = con.prepareStatement(SQL_CREATE_EVENT + "delete_hash" + hash + SQL_EVENT_PROPERTIES);
			k = 1;
			pstmt.setString(k++, hash);
			pstmt.execute();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Failed to insert hash and create a delete event to it");
			throw new DBException("Failed to insert hash and create a Delete Event to it", e);
		} finally {
			close(con, pstmt);
		}
		return hash;
	}
	
	/**
	 * Finds user by password recovery link(hash) in DB and deletes it
	 * 
	 * @param hash
	 * @return User
	 * @throws DBException
	 */
	public User findByHashAndDelete(String hash) throws DBException {
		User user = null;
		
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Connection con = null;

		try {
			con = getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_HASH);
			pstmt.setString(1, hash);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				user = extract(resultSet);
			}
			close(pstmt);
			
			pstmt = con.prepareStatement(SQL_DELETE_HASH);
			pstmt.setString(1, hash);
			pstmt.execute();

			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Failed to get the user and delete hash");
			throw new DBException("Failed to get the user and delete hash", e);
		} finally {
			close(con, pstmt, resultSet);
		}
		return user;
	}
}
