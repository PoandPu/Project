package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;

import javax.servlet.ServletException;
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

public class SwitchLocaleCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -948407284117005741L;
	private static final Logger LOG = Logger.getLogger(SwitchLocaleCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		LOG.debug("Start executing Command");
		String result = null;
		result = doPost(request);
		LOG.debug("Finished executing Command");
		return result;
	}

	private String doPost(HttpServletRequest request) throws DBException {
		HttpSession session = request.getSession();
		UserDAO userDAO = UserDAO.getInstance();
		String lang = request.getParameter("language");
		
		User user = (User) session.getAttribute(AttributeNames.USER);
		LOG.debug("Users language BEFORE " + user.getLanguage());
		if (lang.equals(Fields.EN)) {
			user.setLanguage(Fields.EN);
		} else {
			user.setLanguage(Fields.RU);
		}
		LOG.debug("Users language AFTER " + user.getLanguage());
		userDAO.update(user);

		session.setAttribute(AttributeNames.USER, user);

		return Path.COMMAND_VIEW_LIST_SUBJECTS;	
	}
}
