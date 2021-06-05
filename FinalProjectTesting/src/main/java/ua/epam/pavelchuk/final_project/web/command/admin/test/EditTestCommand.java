package ua.epam.pavelchuk.final_project.web.command.admin.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.TestDAO;
import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.db.validation.TestValidation;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class EditTestCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8665684003534398434L;
	private static final Logger LOG = Logger.getLogger(EditTestCommand.class);

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
		int testId = 0;
		int subjectId = 0;
		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		try {
			TestDAO testDAO = TestDAO.getInstance();
			Test test = testDAO.findTestById(testId);
			request.setAttribute(AttributeNames.TEST, test);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("edit_test_command.error.get", e);
		}
		request.setAttribute(AttributeNames.SUBJECT_ID, subjectId);
		return Path.ADMIN_EDIT_TEST;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int testId = 0;
		int subjectId = 0;
		int time = 0;
		int difficultyLevel = 0;
		
		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
			difficultyLevel = Integer.parseInt(request.getParameter(ParameterNames.TEST_DIFFICULTY));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		String nameRu = request.getParameter(ParameterNames.NAME_RU);
		String nameEn = request.getParameter(ParameterNames.NAME_EN);
		
		//time field validation 
		try {
			time = Integer.parseInt(request.getParameter(ParameterNames.TEST_TIME));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.time_not_number");
			return Path.COMMAND_EDIT_TEST + "&subjectId=" + subjectId + "&testId=" + testId;
		}

		try {
			TestDAO testDAO = TestDAO.getInstance();
			Test test = testDAO.findTestById(testId);

			if (request.getParameter(ParameterNames.DELETE) != null) {
				testDAO.delete(testId);
			}
			
			if (!TestValidation.validate(request, nameRu, nameEn, time, test)) {
				return Path.COMMAND_EDIT_TEST + "&subjectId=" + subjectId + "&testId=" + testId;
			}

			test.setNameRu(nameRu);
			test.setNameEn(nameEn);
			test.setTime(time);
			test.setDifficultyLevel(difficultyLevel);
			testDAO.update(test);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("edit_test_command.error.post", e);
		}
		return Path.COMMAND_VIEW_TESTS_LIST + "&subjectId=" + subjectId;
	}
}
