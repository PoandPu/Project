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
import ua.epam.pavelchuk.final_project.db.validation.TestValidator;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class AddTestCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6240621831935572912L;
	private static final Logger LOG = Logger.getLogger(AddTestCommand.class);

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
		int subjectId = 0;
		try {
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		// it was made to return to the page tests_list page of current subject
		request.setAttribute(AttributeNames.SUBJECT_ID, subjectId);
		LOG.trace(subjectId);
		return Path.ADMIN_ADD_TEST;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int subjectId = 0;
		int difficultyLevel = 0;
		int time = 0;

		try {
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
			difficultyLevel = Integer.parseInt(request.getParameter(ParameterNames.TEST_DIFFICULTY));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		// time validation
		try {
			time = Integer.parseInt(request.getParameter(ParameterNames.TEST_TIME).trim());
		} catch (NumberFormatException ex) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE,
					"test_validation.error.time_not_number");
			return Path.COMMAND_ADD_TEST + "&subjectId=" + subjectId;
		}

		String nameRu = request.getParameter(ParameterNames.NAME_RU);
		String nameEn = request.getParameter(ParameterNames.NAME_EN);

		TestDAO testDAO = null;
		try {
			testDAO = TestDAO.getInstance();
			Test test = new Test();

			if (!TestValidator.validate(request, nameRu, nameEn, time, test)) {
				return Path.COMMAND_ADD_TEST + "&subjectId=" + subjectId;
			}

			test.setNameRu(nameRu);
			test.setNameEn(nameEn);
			test.setTime(time);
			test.setSubjectId(subjectId);
			test.setDifficultyLevel(difficultyLevel);
			testDAO.insert(test);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("add_test_command.error.post", e);
		}
		return Path.COMMAND_VIEW_TESTS_LIST + "&subjectId=" + subjectId;
	}
}
