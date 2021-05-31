package ua.epam.pavelchuk.final_project.db.exception;

/**
 * Error message
 * @author 
 *
 */
public class Messages {
	
	private Messages() {}
	
	public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";
	public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain a connection from pool";
	public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close connection";
	public static final String ERR_CANNOT_OBTAIN_USER_BY_LOGIN = "Cannot obtain user by login";
	public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close statement";
	public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close resultset";
	public static final String ERR_CANNOT_CREATE_USER ="Cannot create a new user";
	public static final String ERR_CANNOT_CHECK_EMAIL ="An error occurred while checking the email";
	public static final String ERR_CANNOT_GET_ENTRANT_ID_BY_USER_ID = "Cannot get entrant id by user id";
	
	public static final String ERR_CANNOT_GET_SUBJECTS ="Cannot get subjects";
	public static final String ERR_CANNOT_DELETE_FACULTY ="Cannot delete faculty";
	public static final String ERR_CANNOT_INSERT_FACULTY ="Cannot insert faculty";
	public static final String ERR_CANNOT_DELETE_SUBJECT ="Cannot delete subject";
	public static final String ERR_CANNOT_INSERT_SUBJECT ="Cannot insert subject";
	public static final String ERR_CANNOT_UPDATE_ENTRANT ="Cannot update subject";
	
}
