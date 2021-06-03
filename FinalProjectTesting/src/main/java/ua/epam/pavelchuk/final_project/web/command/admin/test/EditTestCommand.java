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
		try {
			TestDAO testDAO = TestDAO.getInstance();
			Test test = testDAO.findTestById(Integer.parseInt(request.getParameter(ParameterNames.TEST_ID)));
			int subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
			LOG.debug(test);
			request.setAttribute(AttributeNames.TEST, test);
			// it was made to return to the page tests_list page of current subject
			request.setAttribute(AttributeNames.SUBJECT_ID, subjectId);
			LOG.debug(subjectId);
		} catch (DBException e) {
			LOG.error(Messages.ERR_CANNOT_UPDATE_ENTRANT);
			throw new AppException(Messages.ERR_CANNOT_UPDATE_ENTRANT, e);
		}

		// WRITE A PAGE
		return Path.ADMIN_EDIT_TEST;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		TestDAO testDAO = TestDAO.getInstance();
		int testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));

		// it was made to return to the page tests_list of current subject
		int subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		Test test = testDAO.findTestById(testId);

		if (request.getParameter(ParameterNames.DELETE) != null) {
			testDAO.delete(testId);
		} else {
			String nameRu = request.getParameter(ParameterNames.NAME_RU);
			String nameEn = request.getParameter(ParameterNames.NAME_EN);
			String timeStr = request.getParameter(ParameterNames.TEST_TIME);
			int difficultyLevel = Integer.parseInt(request.getParameter(ParameterNames.TEST_DIFFICULTY));

			if (!TestValidation.validate(request, nameRu, nameEn, timeStr, test)) {
				return Path.COMMAND_EDIT_TEST + "&subjectId=" + subjectId + "&testId=" + testId;
			}

			int time = Integer.parseInt(request.getParameter(ParameterNames.TEST_TIME));

			test.setNameRu(nameRu);
			test.setNameEn(nameEn);
			test.setTime(time);
			test.setDifficultyLevel(difficultyLevel);
			testDAO.update(test);
		}

		return Path.COMMAND_VIEW_TESTS_LIST + "&subjectId=" + subjectId;

	}

}
