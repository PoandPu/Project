package ua.epam.pavelchuk.final_project.db.exception;

/**
 * Application exception
 * @author 
 *
 */
public class AppException  extends Exception{

	private static final long serialVersionUID = -5733037831413544616L;

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
