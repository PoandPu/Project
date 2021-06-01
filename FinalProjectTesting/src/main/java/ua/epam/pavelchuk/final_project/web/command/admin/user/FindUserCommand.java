package ua.epam.pavelchuk.final_project.web.command.admin.user;

import java.io.IOException; 
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class FindUserCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4433959992038742752L;
	private static final Logger LOG = Logger.getLogger(FindUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		String result = null;

		result = doGet(request);

		LOG.debug("Command finished");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		String searchPattern = request.getParameter(ParameterNames.PATTERN);
		try {
			UserDAO userDAO = UserDAO.getInstance();
			List<User> users = userDAO.findUsersLike(searchPattern);
			request.setAttribute(AttributeNames.USERS, users);
		} catch (DBException e) {
			LOG.error("Cannot get a users list from data base");
			throw new AppException("Cannot get a users list from data base", e);
		}
		return Path.ADMIN_PAGE_LIST_USERS;
	}
}
