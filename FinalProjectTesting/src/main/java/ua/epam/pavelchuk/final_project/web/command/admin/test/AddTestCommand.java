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

	private String doGet(HttpServletRequest request) {
		int subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));

		// it was made to return to the page tests_list page of current subject
		request.setAttribute(AttributeNames.SUBJECT_ID, subjectId);
		LOG.debug(subjectId);
		return Path.ADMIN_ADD_TEST;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		// it was made to return to the tests_list page of current subject
		int subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		String nameRu = request.getParameter(ParameterNames.NAME_RU);
		String nameEn = request.getParameter(ParameterNames.NAME_EN);
		String timeStr = request.getParameter(ParameterNames.TEST_TIME);
		int difficultyLevel = Integer.parseInt(request.getParameter(ParameterNames.TEST_DIFFICULTY));

		TestDAO testDAO = null;
		try {
			testDAO = TestDAO.getInstance();
			Test test = new Test();
			
			if(!TestValidation.validate(request, nameRu, nameEn, timeStr, test)) {
				return Path.COMMAND_ADD_TEST + "&subjectId=" + subjectId;
			}
			
			int time = Integer.parseInt(timeStr);
			
			test.setNameRu(nameRu);
			test.setNameEn(nameEn);
			test.setTime(time);
			test.setSubjectId(subjectId);
			test.setDifficultyLevel(difficultyLevel);
			testDAO.insert(test);
		} catch (DBException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		}

		return Path.COMMAND_VIEW_TESTS_LIST + "&subjectId=" + subjectId;
	}
}
