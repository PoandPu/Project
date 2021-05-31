package ua.epam.pavelchuk.final_project.web.command.admin.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class AddQuestion extends Command{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2684928586891220013L;
	private static final Logger LOG = Logger.getLogger(AddQuestion.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		
		String result = doPost(request);
			
		LOG.debug("Command finished");
		return result;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
		int subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		try {
			QuestionDAO questionDAO = QuestionDAO.getInstance();
			Question question = new Question();
			question.setNameRu("Это новый вопрос");
			question.setNameEn("This is a new question");
			question.setTestId(testId);
			questionDAO.insert(question);
		} catch (DBException e) {
			LOG.error("An error occured while adding a new question", e);
			throw new AppException("An error occured while adding a new question", e);
		}
		return Path.COMMAND_VIEW_TEST + "&subjectId=" + subjectId + "&testId=" + testId;
	}

}


