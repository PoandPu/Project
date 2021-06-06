package ua.epam.pavelchuk.final_project.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.CommandContainer;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * Servlet implementation class Controller
 */

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 2423353715955164816L;

	private static final Logger LOG = Logger.getLogger(Controller.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("Try to execute GET command (" + request.getParameter(ParameterNames.COMMAND) + ")");
		String forward = process(request, response, HttpMethod.GET);
		request.getRequestDispatcher(forward).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("Try to execute POST command (" + request.getParameter(ParameterNames.COMMAND) + ")");
		String redirect = process(request, response, HttpMethod.POST);
		response.sendRedirect(redirect);

	}

	/**
	 * Main method of this controller.
	 */
	private String process(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException {
		LOG.debug("Controller starts");

		// extract command name from the request
		String commandName = request.getParameter(ParameterNames.COMMAND);
		LOG.trace("Request parameter: command --> " + commandName);
		LOG.trace(commandName);

		// obtain command object by its name
		Command command = CommandContainer.get(commandName);
		LOG.trace("Obtained command --> " + command);

		// execute command and get forward address
		String forward = Path.PAGE_ERROR;
		try {
			forward = command.execute(request, response, method);
		} catch (AppException ex) {
			LOG.error("Error in controller " + ex.getMessage());
			if (method == HttpMethod.POST) {
				forward = ex.getMessage() != null ? Path.COMMAND_VIEW_ERROR_PAGE + "&errorMessage=" + ex.getMessage()
						: Path.COMMAND_VIEW_ERROR_PAGE;
			} else {
				request.setAttribute("errorMessage", ex.getMessage());
			}

		}
		LOG.trace("Forward address --> " + forward);

		LOG.debug("Controller finished, now go to forward address --> " + forward);

		return forward;
	}
}