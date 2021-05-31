package ua.epam.pavelchuk.final_project.web.command.admin.user;

import java.io.IOException; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.db.validation.UserValidation;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class EditUserCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4433959992038742752L;
	private static final Logger LOG = Logger.getLogger(EditUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {

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

	private String doGet(HttpServletRequest request) throws AppException {
		int userId =  Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
		try {
			UserDAO userDAO = UserDAO.getInstance();
			User user = userDAO.findById(userId);
			if (Role.getRole(user) == Role.ADMIN) {
				throw new AppException("error_page_jsp.edit_admin.error");
			}
			request.setAttribute(AttributeNames.USER, user);
		} catch (DBException e) {
			LOG.error("Cannot obtain a connection");
			throw new AppException();
		}
		return Path.ADMIN_EDIT_USER;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int userId =  Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
		
		try {
			UserDAO entrantDAO = UserDAO.getInstance();	
			User user = entrantDAO.findById(userId);
				
			String firstName = request.getParameter(ParameterNames.USER_FIRST_NAME);
			String lastName = request.getParameter(ParameterNames.USER_LAST_NAME);
			String email = request.getParameter(ParameterNames.USER_EMAIL);
			
			
			if (!UserValidation.validate(request, firstName, lastName, email, user, null, null)) {
				return Path.COMMAND_EDIT_USER + "&userId=" + userId;
			}

			user.setFirstName(request.getParameter(ParameterNames.USER_FIRST_NAME));
			user.setLastName(request.getParameter(ParameterNames.USER_LAST_NAME));
			user.setEmail(request.getParameter(ParameterNames.USER_EMAIL));
			entrantDAO.update(user);
			if (request.getParameter(ParameterNames.RADIO_BUTTON).equals(ParameterNames.BLOCK)) {
				entrantDAO.blockById(userId);
			} else {
				entrantDAO.unblockById(userId);
			}
		} catch (DBException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		}

		return Path.COMMAND_VIEW_USERS_LIST;

	}
}
