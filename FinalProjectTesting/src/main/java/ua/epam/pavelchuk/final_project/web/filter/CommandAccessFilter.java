package ua.epam.pavelchuk.final_project.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * Command access filter
 * 
 * @author O.Pavelchuk
 */
public class CommandAccessFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(CommandAccessFilter.class);

	private EnumMap<Role, List<String>> access = new EnumMap<>(Role.class);
	private List<String> noControl = new ArrayList<>();
	private List<String> commons = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.trace("AccessFilter initialization starts");
		access.put(Role.ADMIN, Arrays.asList(filterConfig.getInitParameter("admin").split("\\s+")));
		access.put(Role.CLIENT, Arrays.asList(filterConfig.getInitParameter("client").split("\\s+")));
		LOG.debug("Access map = " + access);

		commons.addAll(Arrays.asList(filterConfig.getInitParameter("common").split("\\s+")));
		LOG.debug("Commons commands = " + commons);

		noControl.addAll(Arrays.asList(filterConfig.getInitParameter("no_control").split("\\s+")));
		LOG.debug("Without control commands = " + noControl);
		LOG.trace("AccessFilter has been initialized");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOG.trace("Filter starts");
		HttpSession session = ((HttpServletRequest) request).getSession();
		if (checkAccess(request)) {
			LOG.trace("Filter finished");
			chain.doFilter(request, response);
		} else if (session.getAttribute(AttributeNames.USER) == null) {
			LOG.trace("No user found in session, access denied");
			session.setAttribute(AttributeNames.LOGIN_ERROR_MESSAGE, "command_access.error.log_in");
			request.getRequestDispatcher(Path.PAGE_LOGIN).forward(request, response);
		} else if (((User) session.getAttribute(AttributeNames.USER)).getIsBlocked()) {
			LOG.trace("User " + session.getAttribute(AttributeNames.USER) + " is blocked!");
			((HttpServletResponse) response).sendRedirect(Path.COMMAND_LOGOUT);
		} else {
			LOG.trace("User " + session.getAttribute(AttributeNames.USER) + " don't have access to this resource!");
			((HttpServletRequest) request).setAttribute(AttributeNames.ERROR_MESSAGE, "command_access.error.no_root");
			request.getRequestDispatcher(Path.PAGE_ERROR).forward(request, response);
		}

	}

	private boolean checkAccess(ServletRequest req) {
		HttpServletRequest request = (HttpServletRequest) req;

		String command = request.getParameter(ParameterNames.COMMAND);
		LOG.debug("Command = " + command);

		if (noControl.contains(command)) {
			return true;
		}
		if (command == null || command.isEmpty()) {
			return false;
		}
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		Role userRole = (Role) session.getAttribute(AttributeNames.USER_ROLE);
		if (userRole == null) {
			return false;
		}

		// updates user in session and check user status
		try {
			User currentUser = UserDAO.getInstance().findById((int) session.getAttribute(AttributeNames.ID));
			session.setAttribute(AttributeNames.USER, currentUser);
			if (currentUser.getIsBlocked()) {
				return false;
			}
		} catch (DBException e) {
			LOG.error(e);
		}
		return access.get(userRole).contains(command) || commons.contains(command);
	}
}
