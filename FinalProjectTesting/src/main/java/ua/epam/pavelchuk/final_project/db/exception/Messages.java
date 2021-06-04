package ua.epam.pavelchuk.final_project.db.exception;

/**
 * Error message
 * @author O.Pavelchuk
 *
 */
public class Messages {
	
	private Messages() {}
	
	public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";
	public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain a connection from pool";
	public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close connection";
	public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close statement";
	public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close resultset";
	
	
	
	public static final String ERR_CANNOT_CREATE_USER ="Cannot create a new user";
	public static final String ERR_CANNOT_OBTAIN_USER_BY_LOGIN = "Cannot obtain user by login";
	public static final String ERR_CANNOT_CHECK_EMAIL ="An error occurred while checking the email";
	public static final String ERR_CANNOT_GET_ENTRANT_ID_BY_USER_ID = "Cannot get entrant id by user id";
	
	public static final String ERR_CANNOT_GET_SUBJECTS ="Cannot get subjects";
	public static final String ERR_CANNOT_DELETE_SUBJECT ="Cannot delete subject";
	public static final String ERR_CANNOT_INSERT_SUBJECT ="Cannot insert subject";
	public static final String ERR_CANNOT_UPDATE_ENTRANT ="Cannot update subject";
	
	
	public static final String ERR_PARSING_PARAMETERS ="parsing.error";
	public static final String ERR_PARSING_PARAMETERS_LOG ="An error occurred while parsing parameters";
	
	public static final String ERR_ADD_SUBJECT_POST ="add_subject_command.error";
	
	public static final String ERR_EDIT_SUBJECT_POST ="edit_subject_command.error.post";
	public static final String ERR_EDIT_SUBJECT_GET ="edit_subject_command.error.get";
	
	public static final String ERR_ADD_ANSWER_POST ="add_answer_command.error.post";
	public static final String ERR_DELETE_ANSWER_POST ="delete_answer_command.error.post";
	
	public static final String ERR_ADD_QUESTION_POST ="add_question_command.error.post";
	public static final String ERR_DELETE_QUESTION_POST ="delete_question_command.error.post";
	
	public static final String ERR_ADD_TEST_POST ="add_test_command.error.post";
	public static final String ERR_EDIT_TEST_GET ="edit_test_command.error.get";
	public static final String ERR_EDIT_TEST_POST ="edit_test_command.error.post";
	
	public static final String ERR_EDIT_TEST_CONTENT_POST ="edit_test_content_command.error.post";
	public static final String ERR_EDIT_TEST_CONTENT_GET ="edit_test_content_command.error.get";
	
	public static final String ERR_EDIT_USER_POST ="edit_user_command.error.post";
	public static final String ERR_EDIT_USER_GET ="edit_user_command.error.get";
	
}
