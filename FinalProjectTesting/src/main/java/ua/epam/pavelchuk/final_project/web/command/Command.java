package ua.epam.pavelchuk.final_project.web.command;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;

/**
 * Main interface for the Command pattern implementation.
 * 
 * @author O.Pavelchuk
 *
 */

public abstract class Command implements Serializable {

	private static final long serialVersionUID = -96862513789847606L;

	/**
	 * Execution method for command.
	 * 
	 * @return Address to go once the command is executed.
	 */
	public abstract String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws ServletException, IOException, AppException;

	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}
