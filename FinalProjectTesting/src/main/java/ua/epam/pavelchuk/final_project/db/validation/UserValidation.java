package ua.epam.pavelchuk.final_project.db.validation;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;

public class UserValidation {

	private UserValidation() {
	}

	private static final String EMAIL_PATTERN = "^[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
	private static final String LOGIN_PATTERN = "[A-Za-z][A-Za-z\\d]{0,19}";
	private static final String NAME_PATTERN = "[a-zA-Z\\p{IsCyrillic}]{2,45}";

	private static final Logger LOG = Logger.getLogger(UserValidation.class);

	public static boolean validate(HttpServletRequest request, String firstName, String lastName, String email,
			User user, String newPassword, String confirmPassword) throws DBException {
		if (firstName == null || lastName == null || email == null) {
			request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE, "entrant_validation.error.empty_form");
			LOG.debug("Error NULL");
			return false;
		}

		if (!UserValidation.validationName(firstName) || !UserValidation.validationName(lastName)) {
			request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE,
					"entrant_validation.error.invalid_names");
			return false;
		}

		if (!UserValidation.validationEmail(email)) {
			request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE, "entrant_validation.error.invalid_email");
			return false;
		}

		if (user.getEmail() != null && !user.getEmail().equals(email)
				&& !UserValidation.checkUniquenessEmail(email)) {
			request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE, "entrant_validation.error.email_not_unique");
			return false;
		}
		if (newPassword == null && confirmPassword != null || newPassword != null && confirmPassword == null) {
			request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE, "entrant_validation.error.password_mismatch");
			return false;
		}

		if (newPassword != null && !newPassword.equals(confirmPassword)) {
			request.getSession().setAttribute(AttributeNames.USER_SETTINGS_ERROR_MESSAGE, "entrant_validation.error.password_mismatch");
			return false;
		}
		return true;
	}

	public static boolean validationName(String name) {
		return name != null && name.matches(NAME_PATTERN);
	}

	public static boolean validationEmail(String email) {
		return email != null && email.matches(EMAIL_PATTERN);
	}

	public static boolean checkUniquenessEmail(String email) throws DBException {
		if (email == null) {
			return false;
		}
		UserDAO userDAO = UserDAO.getInstance();
		return !userDAO.hasEmail(email);
	}

	public static boolean validationLogin(String login) throws AppException {
		boolean res = false;
		if (login != null && login.matches(LOGIN_PATTERN)) {

			try {
				UserDAO userDAO = UserDAO.getInstance();
				res = !userDAO.hasLogin(login);
			} catch (DBException ex) {
				LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
				throw new AppException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			}
		}
		return res;
	}
}
