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
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;
 
public class ViewUsersListCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1346821585808172634L;
	private static final Logger LOG = Logger.getLogger(ViewUsersListCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = doGet(request);
		LOG.debug("Command finished");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		// pagination
		int page = 1;
		int lines = 10;
		try {
			if (request.getParameter(ParameterNames.PAGINATION_PAGE) != null
					&& !request.getParameter(ParameterNames.PAGINATION_PAGE).isEmpty()) {
				page = Integer.parseInt(request.getParameter(ParameterNames.PAGINATION_PAGE));
			}
			if (request.getParameter(ParameterNames.PAGINATION_LINES) != null
					&& !request.getParameter(ParameterNames.PAGINATION_LINES).isEmpty()) {
				lines = Integer.parseInt(request.getParameter(ParameterNames.PAGINATION_LINES));
			}
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		if (page < 1) {
			page = 1;
		}
		if (lines < 1) {
			lines = 10;
		}
		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "id"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "ASC"
				: request.getParameter(ParameterNames.DIRECTION);
		try {
			UserDAO userDAO = UserDAO.getInstance();
			List<User> users = userDAO.findAllUsersOrderBy(orderBy, direction, (page - 1) * lines, lines);
			while (users.isEmpty() && page > 1) {
				page--;
				users = userDAO.findAllUsersOrderBy(orderBy, direction, (page - 1) * lines, lines);
			}
			request.setAttribute(AttributeNames.USERS, users);
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("view_users_list_command.error.get", ex);
		}
		request.setAttribute(AttributeNames.PAGINATION_LINES, lines);
		request.setAttribute(AttributeNames.PAGINATION_PAGE, page);
		request.setAttribute(AttributeNames.ORDER_BY, orderBy);
		request.setAttribute(AttributeNames.DIRECTION, direction);

		return Path.ADMIN_PAGE_LIST_USERS;
	}
}
