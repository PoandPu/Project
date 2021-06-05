package ua.epam.pavelchuk.final_project.web.command.admin.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class DeleteQuestionCommand extends Command{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2684928586891220013L;
	private static final Logger LOG = Logger.getLogger(DeleteQuestionCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		String result = doPost(request);

		LOG.debug("Command finished");
		return result;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int testId = 0;
		int subjectId = 0;
		int questionId = 0;
		try {
		testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
		subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		questionId = Integer.parseInt(request.getParameter(ParameterNames.QUESTION_ID));
		}catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		try {
			QuestionDAO questionDAO = QuestionDAO.getInstance();
			questionDAO.delete(questionId);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("delete_question_command.error.post", e);
		}
		return Path.COMMAND_VIEW_TEST + "&subjectId=" + subjectId + "&testId=" + testId;
	}
}
