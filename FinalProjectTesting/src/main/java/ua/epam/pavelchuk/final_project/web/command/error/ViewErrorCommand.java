package ua.epam.pavelchuk.final_project.web.command.error;

import java.io.IOException; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.Command;


/**
 * View error page
 * @author 
 *
 */
public class ViewErrorCommand extends Command{

	private static final long serialVersionUID = -3695555195703848425L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		request.setAttribute("errorMessage", request.getParameter("errorMessage"));
		return Path.PAGE_ERROR;
	}

}
