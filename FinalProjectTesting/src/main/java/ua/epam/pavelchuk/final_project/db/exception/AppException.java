package ua.epam.pavelchuk.final_project.db.exception;

/**
 * Application exception
 * 
 * @author O.Pavelchuk
 *
 */
public class AppException extends Exception {

	private static final long serialVersionUID = 7300669526208901741L;

	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}
}
