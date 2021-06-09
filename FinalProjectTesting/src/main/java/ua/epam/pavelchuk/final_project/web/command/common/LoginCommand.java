package ua.epam.pavelchuk.final_project.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.captcha.VerifyUtils;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;
import ua.epam.pavelchuk.final_project.web.password_encryption.PasswordUtils;

/**
 * Login command.
 * 
 * @author
 * 
 */
public class LoginCommand extends Command {

	private static final long serialVersionUID = 8526935211035367501L;
	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

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
		return Path.PAGE_LOGIN;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		HttpSession session = request.getSession();
		User user = null;
		String login = request.getParameter(ParameterNames.LOGIN);
		LOG.trace("Request parameter: login --> " + login);

		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

		if (!VerifyUtils.verify(gRecaptchaResponse)) {
			request.getSession().setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, "CAPTHCA INVALID");
			return Path.COMMAND_VIEW_LOGIN_PAGE;
		}

		String password = request.getParameter(ParameterNames.PASSWORD);
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			request.getSession().setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, "login_jsp.error.empty_form");
			return Path.COMMAND_VIEW_LOGIN_PAGE;
		}

		try {
			UserDAO userDAO = UserDAO.getInstance();
			user = userDAO.findByLogin(login);
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("login_command.error", ex);
		}

		LOG.trace("Found in DB: user --> " + user);
		
		if (user == null
				|| !PasswordUtils.generateSecurePassword(password, user.getPasswordKey()).equals(user.getPassword())) {
			request.getSession().setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, "login_jsp.error.not_found");
			return Path.COMMAND_VIEW_LOGIN_PAGE;
		}

		// Check block
		if (user.getIsBlocked()) {
			request.getSession().setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, "login_jsp.error.blocked");
			return Path.COMMAND_VIEW_LOGIN_PAGE;
		}

		Role userRole = Role.getRole(user);
		LOG.trace("userRole --> " + userRole);

		String result = Path.COMMAND_VIEW_LIST_SUBJECTS;

		session.setAttribute(AttributeNames.USER, user);
		LOG.trace("Set the session attribute: user --> " + user);

		session.setAttribute(AttributeNames.ID, user.getId());
		LOG.trace("Set the session attribute: userId --> " + user.getId());

		session.setAttribute(AttributeNames.USER_ROLE, userRole);
		LOG.trace("Set the session attribute: userRole --> " + userRole);

		LOG.info("User " + user + " logged as " + userRole.toString().toLowerCase());

		return result;
	}
}