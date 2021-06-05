package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.SubjectDAO;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * View all subjects
 * 
 * @author O.Pavelchuk
 *
 */
public class ViewAllSubjectsCommand extends Command {

	private SubjectDAO subjectDAO;

	public ViewAllSubjectsCommand() {
		try {
			subjectDAO = SubjectDAO.getInstance();
		} catch (DBException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE);
		}
	}

	public ViewAllSubjectsCommand(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	private static final long serialVersionUID = 9188012295034506682L;
	private static final Logger LOG = Logger.getLogger(ViewAllSubjectsCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		LOG.debug("Start executing Command");
		String result = doGet(request);
		LOG.debug("Finished executing Command");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		List<Subject> subjects = null;

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

		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "id"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "ASC"
				: request.getParameter(ParameterNames.DIRECTION);

		try {
			subjects = subjectDAO.findAllOrderBy(orderBy, direction, (page - 1) * lines, lines);
			while (subjects.isEmpty() && page > 1) {
				page--;
				subjects = subjectDAO.findAllOrderBy(orderBy, direction, (page - 1) * lines, lines);
			}
			request.setAttribute(AttributeNames.SUBJECTS, subjects);
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("view_subjects_command.error.get", ex);
		}
		request.setAttribute(AttributeNames.PAGINATION_LINES, lines);
		request.setAttribute(AttributeNames.PAGINATION_PAGE, page);
		request.setAttribute(AttributeNames.ORDER_BY, orderBy);
		request.setAttribute(AttributeNames.DIRECTION, direction);

		// Admin page
		if (request.getSession().getAttribute(AttributeNames.USER_ROLE) == Role.ADMIN) {
			return Path.ADMIN_PAGE_LIST_ALL_SUBJECTS;
		}
		// User page
		return Path.PAGE_LIST_ALL_SUBJECTS;
	}
}