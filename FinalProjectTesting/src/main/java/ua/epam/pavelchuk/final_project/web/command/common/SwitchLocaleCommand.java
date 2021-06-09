package ua.epam.pavelchuk.final_project.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Fields;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class SwitchLocaleCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -948407284117005741L;
	private static final Logger LOG = Logger.getLogger(SwitchLocaleCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Start executing Command");
		String result = null;
		result = doPost(request);
		LOG.debug("Finished executing Command");
		return result;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		HttpSession session = request.getSession();
		String lang = request.getParameter(ParameterNames.LANGUAGE);
		User user = (User) session.getAttribute(AttributeNames.USER);

		LOG.debug("Users language BEFORE " + user.getLanguage());
		LOG.debug("Users password" + user.getPassword());
		LOG.debug("Users password key" + user.getPasswordKey());
		if (lang.equals(Fields.EN)) {
			user.setLanguage(Fields.EN);
		} else {
			user.setLanguage(Fields.RU);
		}
		try {
			UserDAO userDAO = UserDAO.getInstance();
			userDAO.update(user);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("switch_locale_command.error", e);
		}
		LOG.debug("Users language AFTER " + user.getLanguage());

		session.setAttribute(AttributeNames.USER, user);

		return Path.COMMAND_VIEW_LIST_SUBJECTS;
	}
}
