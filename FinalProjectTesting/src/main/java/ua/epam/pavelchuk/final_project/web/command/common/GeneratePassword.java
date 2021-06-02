package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;

import javax.servlet.ServletException;
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

public class GeneratePassword extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4819639561973518537L;
	private static final Logger LOG = Logger.getLogger(GeneratePassword.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, 
			HttpMethod method) throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		String result = null;
		if(method == HttpMethod.POST) {
			result = doPost(request);
		} else {
			result = doGet(request);
		}
		LOG.debug("Command finished");
		return result;
	}
	
	private String doGet(HttpServletRequest request) {
		String message = request.getParameter(ParameterNames.HASH);
		LOG.debug(message);
		if (message != null) {
		request.setAttribute(ParameterNames.MESSAGE, message);
		}
		return Path.PAGE_PASSWORD_RECOVERY;
	}
	
	private String doPost(HttpServletRequest request) throws AppException {
		String hash = request.getParameter(ParameterNames.HASH);
		String message = null;
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
			message = "A new password has been successfully generated and sent to email: " + user.getEmail();
			}
		} catch (DBException ex) {
			LOG.error(ex);
		}
		
		
		return Path.COMMAND_PASSWORD_RECOVERY + "&message=" + message;
	}
}
