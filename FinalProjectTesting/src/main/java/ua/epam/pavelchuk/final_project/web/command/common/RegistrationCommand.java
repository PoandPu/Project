package ua.epam.pavelchuk.final_project.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Fields;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.validation.UserValidator;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.password_encryption.PasswordUtils;

/**
 * Registration command
 * 
 * @author O.Pavelchuk
 */
public class RegistrationCommand extends Command {

	private static final long serialVersionUID = 6940385928019345652L;
	private static final Logger LOG = Logger.getLogger(RegistrationCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Start executing Command");
		String result = null;

		if (method == HttpMethod.POST) {
			result = doPost(request);
		} else {
			result = doGet();
		}
		LOG.debug("Finished executing Command");
		return result;
	}

	private String doGet() {
		return Path.PAGE_CLIENT_REGISRTATION;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		String login = request.getParameter(Fields.USER_LOGIN);
		String password = request.getParameter(Fields.USER_PASSWORD);
		String confirmPassword = request.getParameter(Fields.USER_CONFIRM_PASSWORD);
		String email = request.getParameter(Fields.USER_EMAIL);
		String firstName = request.getParameter(Fields.USER_FIRST_NAME);
		String lastName = request.getParameter(Fields.USER_LAST_NAME);
		String language = request.getParameter(Fields.USER_LANGUAGE);

		User user = new User();
		try {
			if (!UserValidator.validationLogin(login)) {
				request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE,
						"registration_jsp.error.login_not_unique");
				return Path.COMMAND_VIEW_REGISTRATION_PAGE;
			}
			if (!UserValidator.validate(request, firstName, lastName, email, user, password, confirmPassword)) {
				return Path.COMMAND_VIEW_REGISTRATION_PAGE;
			}
			String passwordKey = PasswordUtils.getSalt(30);
			String mySecurePassword = PasswordUtils.generateSecurePassword(password, passwordKey);

			user.setLogin(login);
			user.setPassword(mySecurePassword);
			user.setPasswordKey(passwordKey);
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setLanguage(language);
			user.setRoleId(Role.CLIENT.ordinal());

			UserDAO userDAO = UserDAO.getInstance();
			userDAO.insert(user);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("registration_command.error.post", e);
		}
		return Path.COMMAND_VIEW_LOGIN_PAGE;
	}

}
