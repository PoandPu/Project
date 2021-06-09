package ua.epam.pavelchuk.final_project.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;
import ua.epam.pavelchuk.final_project.web.mail.SendMail;

/**
 * Manages the password recovery process for a user
 * 
 * @author O.Pavelchuk
 */
public class PasswordRecoveryCommand extends Command {

	private static final long serialVersionUID = 5734516718078837289L;
	private static final Logger LOG = Logger.getLogger(PasswordRecoveryCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = null;
		if (method == HttpMethod.POST) {
			result = doPost(request);
		} else {
			result = doGet(request);
		}
		LOG.debug("Command finished");
		return result;
	}

	private String doGet(HttpServletRequest request) {
		String message = request.getParameter(ParameterNames.MESSAGE);
		String email = request.getParameter(ParameterNames.USER_EMAIL);
		LOG.trace(message);
		if (message != null) {
			if (message.equals("script1")) {
				message = "A letter will now come to the mail " + email
						+ ", specified during registration. It will contain a link that should be followed so that we can create a temporary password. It is very important not to forget to check the \"SPAM\" folder in your mailbox if the letter does not arrive for a long time!";
			} else if (message.equals("script2")) {
				message = "A new password has been successfully generated and sent to email: " + email;
			} else {
				message = "This link to change password is no longer valid ";
			}
			request.setAttribute(ParameterNames.MESSAGE, message);
		}
		return Path.PAGE_PASSWORD_RECOVERY;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		String pattern = request.getParameter(ParameterNames.PATTERN).trim();
		String message = null;
		String email = null;
		User user = null;
		UserDAO userDAO = null;
		try {
			userDAO = UserDAO.getInstance();
			user = userDAO.findUserByLoginMail(pattern);
			LOG.trace(user);

			if (user == null) {
				request.getSession().setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE,
						"login_jsp.error.not_found_password_recovery");
				return Path.COMMAND_PASSWORD_RECOVERY;
			}

			String hash = userDAO.createHash(user);
			LOG.trace("Sending a message to : " + user.getEmail());
			SendMail.sendPasswordRecoveryLink(user.getEmail(), user.getLogin(), hash);

			email = user.getEmail().replaceFirst("[\\S]{2,5}@[\\S]{2,3}", "******");
			message = "script1";
			
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("password_recovery_command.error", ex);
		}

		return Path.COMMAND_PASSWORD_RECOVERY + "&message=" + message + "&email=" + email;
	}

}
