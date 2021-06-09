package ua.epam.pavelchuk.final_project.web.command.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;

/**
 * View error page
 * 
 * @author O.Pavelchuk
 *
 */
public class ViewErrorCommand extends Command {

	private static final long serialVersionUID = 543044319988306036L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		request.setAttribute(AttributeNames.ERROR_MESSAGE, request.getParameter(AttributeNames.ERROR_MESSAGE));
		return Path.PAGE_ERROR;
	}
}
