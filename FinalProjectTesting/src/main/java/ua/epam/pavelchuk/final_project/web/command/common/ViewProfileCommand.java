package ua.epam.pavelchuk.final_project.web.command.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.ResultDAO;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.Result;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * View profile command with results and information about the user
 * 
 * @author O.Pavelchuk
 */
public class ViewProfileCommand extends Command {

	private static final long serialVersionUID = -8555076149696805379L;
	private static final Logger LOG = Logger.getLogger(ViewProfileCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Start executing Command");
		String result = null;
		result = doGet(request);
		LOG.debug("Finished executing Command");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		HttpSession session = request.getSession();
		int currentUserId = (int) session.getAttribute(AttributeNames.ID);
		int userId = Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
		LOG.trace("UserId from url :" + userId);
		LOG.trace("Current userId from url :" + currentUserId);

		// to prohibit viewing other people's profiles through URL
		if (session.getAttribute(AttributeNames.USER_ROLE) != Role.ADMIN && userId != currentUserId) {
			LOG.warn("User with id[" + currentUserId + "] has tried to acces profile with id :" + userId);
			throw new AppException("command_access.error.no_root");
		}

		int page = 1;
		int lines = 10;
		try {
			if (request.getParameter(ParameterNames.PAGINATION_PAGE) != null) {
				page = Integer.parseInt(request.getParameter(ParameterNames.PAGINATION_PAGE));
			}

			if (request.getParameter(ParameterNames.PAGINATION_LINES) != null) {
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

		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "test_date"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "DESC"
				: request.getParameter(ParameterNames.DIRECTION);

		UserDAO userDAO = null;
		ResultDAO resultDAO = null;
		User user = null;
		List<Result> results = null;
		try {
			userDAO = UserDAO.getInstance();
			resultDAO = ResultDAO.getInstance();

			user = userDAO.findById(userId);
			LOG.debug("Founded user :" + user);
			if (user == null) {
				LOG.warn("No user with id[" + userId + "] found");
				throw new AppException("view_profile_command.error.no_user_found");
			}
			results = resultDAO.findResultsByUserIdAllOrderedBy(userId, orderBy, direction, (page - 1) * lines, lines);

			while (results.isEmpty() && page > 1) {
				page--;
				results = resultDAO.findResultsByUserIdAllOrderedBy(userId, orderBy, direction, (page - 1) * lines,
						lines);
			}
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("view_profile_command.error", e);
		}
		request.setAttribute(AttributeNames.RESULTS, results);
		request.setAttribute(AttributeNames.USER, user);
		request.setAttribute(AttributeNames.PAGINATION_LINES, lines);
		request.setAttribute(AttributeNames.PAGINATION_PAGE, page);
		request.setAttribute(AttributeNames.ORDER_BY, orderBy);
		request.setAttribute(AttributeNames.DIRECTION, direction);

		return Path.PAGE_PROFILE;
	}
}
