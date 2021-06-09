package ua.epam.pavelchuk.final_project.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.validation.UserValidator;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;
import ua.epam.pavelchuk.final_project.web.password_encryption.PasswordUtils;

/**
 * User settings command
 * 
 * @author O.Pavelchuk
 */
public class UserSettingsCommand extends Command {

	private static final long serialVersionUID = -1060523174517623317L;
	private static final Logger LOG = Logger.getLogger(UserSettingsCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = null;

		if (method == HttpMethod.POST) {
			result = doPost(request);
		} else {
			result = doGet();
		}
		LOG.debug("Command finished");
		return result;
	}

	private String doGet() {
		return Path.PAGE_USER_SETTINGS;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(AttributeNames.USER);
		String firstName = request.getParameter(ParameterNames.USER_FIRST_NAME);
		String lastName = request.getParameter(ParameterNames.USER_LAST_NAME);
		String email = request.getParameter(ParameterNames.USER_EMAIL);
		String newPassword = request.getParameter(ParameterNames.NEW_PASSWORD);
		String confirmPassword = request.getParameter(ParameterNames.CONFIRM_NEW_PASSWORD);
		try {
			if (!UserValidator.validate(request, firstName, lastName, email, user, newPassword, confirmPassword)) {
				return Path.COMMAND_SETTINGS_USER;
			}

			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			if (!newPassword.isEmpty()) {
				String passwordKey = user.getPasswordKey();
				String securePassword = PasswordUtils.generateSecurePassword(newPassword, passwordKey);
				user.setPassword(securePassword);
			}

			UserDAO userDAO = UserDAO.getInstance();
			userDAO.update(user);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("user_settings_command.error.post", e);
		}
		return Path.COMMAND_VIEW_LIST_SUBJECTS;
	}
}
