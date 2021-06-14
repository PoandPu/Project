package ua.epam.pavelchuk.final_project.web.command.admin.user;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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

public class FindBestUsersCommand extends Command{
	
	private static final long serialVersionUID = 4433959992038742752L;
	private static final Logger LOG = Logger.getLogger(FindBestUsersCommand.class);
	private static final String CONFIG_FILE = System.getProperty("catalina.home") + "\\logs\\temp\\settings.properties";

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = null;

		result = doGet(request);

		LOG.debug("Command finished");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		int numbOfUsers = 0;
		int fromPercentage = 0;
		Properties config = new Properties();
		try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
			config.load(fis);
			numbOfUsers = Integer.parseInt(config.getProperty("number_of_best_users"));
			fromPercentage = Integer.parseInt(config.getProperty("best_user_from_percentage"));
			LOG.trace("Number of users: " + numbOfUsers);
			LOG.trace("Search if result is greater than (%): " + fromPercentage);
		} catch (IOException e) {
			LOG.error("Failed to read properties file");
			throw new AppException("Failed to read properties file", e);
		} catch (NumberFormatException ex) {
			LOG.error("Parsing error, cannot parse to int: " + config.getProperty("number_of_best_users"));
			throw new AppException("Failed to parse info from config file", ex);
		}
		
		String period = request.getParameter("period");
		
		String orderBy = request.getParameter(ParameterNames.ORDER_BY) == null ? "id"
				: request.getParameter(ParameterNames.ORDER_BY);
		String direction = request.getParameter(ParameterNames.DIRECTION) == null ? "ASC"
				: request.getParameter(ParameterNames.DIRECTION);

		try {
			UserDAO userDAO = UserDAO.getInstance();
			List<User> users = userDAO.findBestUsers(period, numbOfUsers, fromPercentage);
			request.setAttribute(AttributeNames.USERS, users);
			LOG.debug(users);
			LOG.debug(users.size());
		} catch (DBException e) {
			LOG.error(Arrays.toString(e.getStackTrace()));
			throw new AppException("find_user_command.error.get", e);
		}

		request.setAttribute(AttributeNames.ORDER_BY, orderBy);
		request.setAttribute(AttributeNames.DIRECTION, direction);
		return Path.ADMIN_PAGE_LIST_USERS;
	}
}