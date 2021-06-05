package ua.epam.pavelchuk.final_project.web.command.admin.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class AddAnswerCommand extends Command {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2684928586891220013L;
	private static final Logger LOG = Logger.getLogger(AddAnswerCommand.class);

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
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		
		try {
			AnswerDAO answerDAO = AnswerDAO.getInstance();
			Answer answer = new Answer();
			answer.setNameRu("Это новый ответ");
			answer.setNameEn("This is a new answer");
			answer.setQuestionId(questionId);
			answer.setIsCorrect(false);
			answerDAO.insert(answer);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("add_answer_command.error.post", e);
		}
		return Path.COMMAND_EDIT_TEST_CONTENT + "&subjectId=" + subjectId + "&testId=" + testId + "&questionId="
				+ questionId;
	}
}
