package ua.epam.pavelchuk.final_project.web.command.admin.user;

import java.util.List;

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

/**
 * Finds users by pattern
 * 
 * @author O.Pavelchuk
 */
public class FindUserCommand extends Command {

	private static final long serialVersionUID = 4433959992038742752L;
	private static final Logger LOG = Logger.getLogger(FindUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = null;

		result = doGet(request);

		LOG.debug("Command finished");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		String searchPattern = request.getParameter(ParameterNames.PATTERN);
		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "id"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "ASC"
				: request.getParameter(ParameterNames.DIRECTION);

		try {
			UserDAO userDAO = UserDAO.getInstance();
			List<User> users = userDAO.findUsersLike(searchPattern);
			request.setAttribute(AttributeNames.USERS, users);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("find_user_command.error.get", e);
		}

		request.setAttribute(AttributeNames.ORDER_BY, orderBy);
		request.setAttribute(AttributeNames.DIRECTION, direction);
		return Path.ADMIN_PAGE_LIST_USERS;
	}
}
