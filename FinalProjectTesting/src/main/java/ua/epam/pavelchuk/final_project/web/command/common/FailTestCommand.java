package ua.epam.pavelchuk.final_project.web.command.common;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.ResultDAO;
import ua.epam.pavelchuk.final_project.db.entity.Result;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * Deletes and put 0 mark by test
 * 
 * @author O.Pavelchuk
 */
public class FailTestCommand extends Command {

	private static final long serialVersionUID = -1722277704315934800L;
	private static final Logger LOG = Logger.getLogger(FailTestCommand.class);

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
		int testId = 0;
		int subjectId = 0;
		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		HttpSession session = request.getSession();
		int currentUserId = (int) session.getAttribute(AttributeNames.ID);

		ResultDAO resultDAO = null;
		try {
			resultDAO = ResultDAO.getInstance();
			Result testResult = new Result(BigDecimal.valueOf(0), currentUserId, testId);
			resultDAO.insert(testResult);
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("check_test_error", ex);
		} finally {
			session.removeAttribute(AttributeNames.TEST_END_TIME);
			session.removeAttribute(AttributeNames.TEST);
			session.removeAttribute(AttributeNames.SUBJECT);
		}
		return Path.COMMAND_VIEW_TESTS_LIST + "&subjectId=" + subjectId;
	}
}
