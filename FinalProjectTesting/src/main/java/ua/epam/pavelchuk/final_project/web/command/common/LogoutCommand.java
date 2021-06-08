package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;


/**
 * Logout
 * @author 
 *
 */
public class LogoutCommand extends Command{

	private static final long serialVersionUID = 6389772507392407802L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		
		HttpSession session= request.getSession();
		String error = (String) session.getAttribute(AttributeNames.LOGIN_ERROR_MESSAGE);
		session.invalidate();
		//session.setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, error);
		request.setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, error);
		return Path.PAGE_LOGIN;
	}

}
