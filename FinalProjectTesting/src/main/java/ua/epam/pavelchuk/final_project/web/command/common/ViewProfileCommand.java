package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
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
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class ViewProfileCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8555076149696805379L;
	private static final Logger LOG = Logger.getLogger(ViewProfileCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
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

		// to prohibit viewing other people's profiles through URL
		if (session.getAttribute(AttributeNames.USER_ROLE) != Role.ADMIN && userId != currentUserId) {
			throw new AppException("command_access.error.no_root");
		}

		UserDAO userDAO = null;
		ResultDAO resultDAO = null;
		User user = null;
		try {
			userDAO = UserDAO.getInstance();
			user = userDAO.findById(userId);
			resultDAO = ResultDAO.getInstance();
		} catch (DBException e) {
			LOG.error("Error");
			throw new AppException();
		}

		int page = 1;
		int lines = 10;
		if (request.getParameter(ParameterNames.PAGINATION_PAGE) != null) {
			page = Integer.parseInt(request.getParameter(ParameterNames.PAGINATION_PAGE));
		}
		if (page < 1) {
			page = 1;
		}
		if (request.getParameter(ParameterNames.PAGINATION_LINES) != null) {
			lines = Integer.parseInt(request.getParameter(ParameterNames.PAGINATION_LINES));
		}
		if (lines < 1) {
			lines = 10;
		}

		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "test_date"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "DESC"
				: request.getParameter(ParameterNames.DIRECTION);

		List<Result> results = resultDAO.getResultsByUserId(userId, orderBy, direction, (page - 1) * lines, lines);

		while (results.isEmpty() && page > 1) {
			page--;
			results = resultDAO.getResultsByUserId(userId, orderBy, direction, (page - 1) * lines, lines);
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
