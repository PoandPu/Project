package ua.epam.pavelchuk.final_project.web.command.admin.user;

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
import ua.epam.pavelchuk.final_project.db.validation.UserValidator;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * Edits a users email, name, status Blocked/Unblocked
 * 
 * @author O.Pavelchuk
 */
public class EditUserCommand extends Command {

	private static final long serialVersionUID = 4433959992038742752L;
	private static final Logger LOG = Logger.getLogger(EditUserCommand.class);

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

	private String doGet(HttpServletRequest request) throws AppException {
		int userId = 0;
		try {
			userId = Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		try {
			UserDAO userDAO = UserDAO.getInstance();
			User user = userDAO.findById(userId);
			if (user == null) {
				LOG.error("No user with id[" + userId + "] found");
				throw new AppException("view_profile_command.error.no_user_found");
			}
			if (Role.getRole(user) == Role.ADMIN) {
				throw new AppException("error_page_jsp.edit_admin.error");
			}
			request.setAttribute(AttributeNames.USER, user);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("edit_user_command.error.get", e);
		}
		return Path.ADMIN_EDIT_USER;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int userId = 0;
		try {
			userId = Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		try {
			UserDAO userDAO = UserDAO.getInstance();
			User user = userDAO.findById(userId);

			String firstName = request.getParameter(ParameterNames.USER_FIRST_NAME);
			String lastName = request.getParameter(ParameterNames.USER_LAST_NAME);
			String email = request.getParameter(ParameterNames.USER_EMAIL);

			if (!UserValidator.validate(request, firstName, lastName, email, user, null, null)) {
				return Path.COMMAND_EDIT_USER + "&userId=" + userId;
			}

			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			userDAO.update(user);
			if (request.getParameter(ParameterNames.RADIO_BUTTON).equals(ParameterNames.BLOCK)) {
				userDAO.blockById(userId);
			} else {
				userDAO.unblockById(userId);
			}
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("edit_user_command.error.post", e);
		}

		return Path.COMMAND_VIEW_USERS_LIST;

	}
}
