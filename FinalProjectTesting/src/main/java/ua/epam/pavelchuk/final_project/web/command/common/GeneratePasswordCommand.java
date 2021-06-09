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
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;
import ua.epam.pavelchuk.final_project.web.mail.SendMail;
import ua.epam.pavelchuk.final_project.web.password_encryption.PasswordUtils;

public class GeneratePasswordCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4819639561973518537L;
	private static final Logger LOG = Logger.getLogger(GeneratePasswordCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = null;
		result = doPost(request);
		LOG.debug("Command finished");
		return result;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		String hash = request.getParameter(ParameterNames.HASH);
		String message = null;
		String email = null;
		UserDAO userDAO = null;
		try {
			userDAO = UserDAO.getInstance();
			User user = userDAO.findByHashAndDelete(hash);

			if (user == null) {
				message = "This link to change password is no longer valid";
			} else {
				String newPassword = PasswordUtils.getSalt(10);
				String passwordKey = user.getPasswordKey();
				String securePassword = PasswordUtils.generateSecurePassword(newPassword, passwordKey);
				user.setPassword(securePassword);

				userDAO.update(user);

				SendMail.sendNewPassword(user.getEmail(), newPassword);
				message = "script2";
				email = user.getEmail();
			}
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("check_test_error", ex);
		}
		return Path.COMMAND_PASSWORD_RECOVERY + "&message=" + message + "&email=" + email;
	}
}
