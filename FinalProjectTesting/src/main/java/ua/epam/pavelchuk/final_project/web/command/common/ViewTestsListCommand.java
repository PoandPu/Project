package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.TestDAO;
import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class ViewTestsListCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4529481474754261462L;
	private static final Logger LOG = Logger.getLogger(ViewTestsListCommand.class);

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
		List<Test> tests = null;
		int subjectId = 0;

		int page = 1;
		int lines = 10;
		try {
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));

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

		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "id"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "ASC"
				: request.getParameter(ParameterNames.DIRECTION);

		try {
			TestDAO testDAO = TestDAO.getInstance();
			tests = testDAO.findTestBySubjectIdAllOrderBy(subjectId, orderBy, direction, (page - 1) * lines, lines);

			while (tests.isEmpty() && page > 1) {
				page--;
				tests = testDAO.findTestBySubjectIdAllOrderBy(subjectId, orderBy, direction, (page - 1) * lines, lines);
			}
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("view_tests_list_command.error.get", ex);
		}

		request.setAttribute(AttributeNames.TESTS, tests);
		request.setAttribute(ParameterNames.SUBJECT_ID, subjectId);
		request.setAttribute(AttributeNames.PAGINATION_LINES, lines);
		request.setAttribute(AttributeNames.PAGINATION_PAGE, page);
		request.setAttribute(AttributeNames.ORDER_BY, orderBy);
		request.setAttribute(AttributeNames.DIRECTION, direction);
		// Admin page
		if (request.getSession().getAttribute(AttributeNames.USER_ROLE) == Role.ADMIN) {
			return Path.ADMIN_PAGE_LIST_TESTS;
		}
		// User page
		return Path.PAGE_LIST_TESTS;
	}
}
