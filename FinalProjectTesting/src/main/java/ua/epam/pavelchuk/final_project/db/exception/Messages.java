package ua.epam.pavelchuk.final_project.db.exception;

/**
 * Error messages
 * @author O.Pavelchuk
 *
 */
public class Messages {
	
	private Messages() {}
	
	public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";
	public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain the connection from pool";
	public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close the connection";
	public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close the statement";
	public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close the resultset";
	public static final String ERR_CANNOT_ROLLBACK_TRANSACTION = "Cannot close the transaction";
	
	public static final String ERR_PARSING_PARAMETERS ="parsing.error";
	public static final String ERR_PARSING_PARAMETERS_LOG ="An error occurred while parsing parameters";
	
	public static final String ERR_EMAIL = "send_email.error";
	public static final String ERR_EMAIL_LOG = "Failed to send the email";
}
