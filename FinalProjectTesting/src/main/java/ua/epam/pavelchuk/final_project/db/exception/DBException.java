package ua.epam.pavelchuk.final_project.db.exception;

import java.sql.SQLException;

/**
 * Data Base exception
 * 
 * @author O.Pavelchuk
 *
 */
public class DBException extends SQLException {

	private static final long serialVersionUID = 8485907141633582613L;

	public DBException() {
		super();
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

}
